package com.panda.article.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.panda.api.config.RabbitDelayedMqConfig;
import com.panda.api.service.BaseService;
import com.panda.article.mapper.ArticleMapper;
import com.panda.article.mapper.ArticleMapperCustom;
import com.panda.article.service.ArticleService;
import com.panda.enums.ArticlePublishType;
import com.panda.enums.ArticleReviewStatus;
import com.panda.enums.YesNoType;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.Article;
import com.panda.pojo.Category;
import com.panda.pojo.bo.CreateArticleBo;
import com.panda.pojo.eo.ArticleEO;
import com.panda.utils.PaginationResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.n3r.idworker.Sid;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleMapperCustom articleMapperCustom;

    @Autowired
    Sid sid;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    private static final Integer GENERAL_REVIEWING_STATUS = 12; // it includes both automatic and manual reviewing

    @Transactional
    @Override
    public void createArticle(CreateArticleBo bo, Category category) {
        String articleId = sid.nextShort();
        Article article = new Article();
        BeanUtils.copyProperties(bo, article);
        article.setId(articleId);
        article.setCategoryId(category.getId());
        article.setArticleStatus(ArticleReviewStatus.reviewing.type);
        article.setCommentCounts(0);
        article.setReadCounts(0);
        article.setIsDelete(YesNoType.no.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        if (article.getIsAppoint() == ArticlePublishType.scheduled.type) {
            article.setPublishTime(bo.getPublishTime());
        } else {
            article.setPublishTime(new Date());
        }
        int rowNum = articleMapper.insert(article);
        if (rowNum != 1) {
            EncapsulatedException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
        if (article.getIsAppoint() == ArticlePublishType.scheduled.type) {
            Date publishTime = bo.getPublishTime();
            Date curTime = new Date();
            int delayDuration = (int)(publishTime.getTime() - curTime.getTime());
            delayDuration = 10 *1000;
            MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties()
                            .setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties()
                            .setDelay(10 * 1000);
                    return message;
                }
            };
            try {
                rabbitTemplate.convertAndSend(
                        RabbitDelayedMqConfig.DELAYED_EXCHANGE_NAME,
                        RabbitDelayedMqConfig.DELAYED_PUBLISH_ARTICLE_ROUTING_KEY,
                        articleId,
                        messagePostProcessor
                );
            } catch (Exception e) {
                e.printStackTrace();
                EncapsulatedException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
            }
        }
    }

    @Transactional
    @Override
    public void updateScheduledArticleToAdhoc() {
        articleMapperCustom.updateScheduledArticleToAdhoc();
    }

    @Override
    public PaginationResult listArticles(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (ArticleReviewStatus.isValidStatus(status)) {
            criteria.andEqualTo("articleStatus", status);
        }
        if (status != null && status == GENERAL_REVIEWING_STATUS) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.reviewing.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.waitingManual.type);
        }
        criteria.andEqualTo("isDelete", YesNoType.no.type);
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("publishTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("publishTime", endDate);
        }
        PageHelper.startPage(page, pageSize);
        List<Article> articles = articleMapper.selectByExample(example);
        return paginationResultBuilder(articles, page);
    }

    @Override
    public PaginationResult listArticlesWithStatus(Integer status, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        if (ArticleReviewStatus.isValidStatus(status)) {
            criteria.andEqualTo("articleStatus", status);
        }
        if (status != null && status == GENERAL_REVIEWING_STATUS) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.reviewing.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.waitingManual.type);
        }

        PageHelper.startPage(page, pageSize);
        List<Article> articles = articleMapper.selectByExample(example);
        return paginationResultBuilder(articles, page);
    }

    @Override
    public void deleteArticle(String userId, String articleId) {
        Example example = exampleBuilder(userId, articleId);
        Article article = new Article();
        article.setIsDelete(YesNoType.yes.type);
        if (articleMapper.updateByExampleSelective(article, example) != 1) {
            EncapsulatedException.display(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
    }

    @Override
    public void withdrawArticle(String userId, String articleId) {
        Example example = exampleBuilder(userId, articleId);
        Article article = new Article();
        article.setArticleStatus(ArticleReviewStatus.withdraw.type);
        if (articleMapper.updateByExampleSelective(article, example) != 1) {
            EncapsulatedException.display(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
    }

    @Override
    public void saveArticleGridFsFileId(String articleId, String fileId) {
        Article article = new Article();
        article.setId(articleId);
        article.setMongoFileId(fileId);
        if (articleMapper.updateByPrimaryKeySelective(article) != 1) {
            EncapsulatedException.display(ResponseStatusEnum.ARTICLE_MONGO_FILE_ID_UPDATE_ERROR);
        }
    }

    @Override
    public void publishArticle(String articleId) {
        Article article = new Article();
        article.setId(articleId);
        article.setIsAppoint(ArticlePublishType.adhoc.type);
        articleMapper.updateByPrimaryKeySelective(article);
    }

    @Transactional
    @Override
    public void updateArticleReviewStatus(String articleId, Integer status) throws IOException {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);
        Article article = new Article();
        article.setArticleStatus(status);
        if (articleMapper.updateByExampleSelective(article, example) != 1) {
            EncapsulatedException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }

        // save review passed article to elasticsearch
        if (status == ArticleReviewStatus.success.type) {
            Article result = articleMapper.selectByPrimaryKey(articleId);
            if (result.getIsAppoint() == ArticlePublishType.adhoc.type) {
                ArticleEO articleEO = new ArticleEO();
                BeanUtils.copyProperties(result, articleEO);
                IndexRequest request = new IndexRequest("users");
                request.id(articleId);
                request.source(new ObjectMapper().writeValueAsString(articleEO), XContentType.JSON);
                try {
                    elasticsearchClient.index(request, RequestOptions.DEFAULT);
                }catch (Exception e) {
                    System.out.println(e);
                }
            } else {
                // ToDo: scheduled article should be saved to elasticsearch when it is published
            }
        }
    }

    private Example exampleBuilder(String userId, String articleId) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        criteria.andEqualTo("id", articleId);
        return example;
    }
}
