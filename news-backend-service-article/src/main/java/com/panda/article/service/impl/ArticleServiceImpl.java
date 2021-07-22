package com.panda.article.service.impl;

import com.github.pagehelper.PageHelper;
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
import com.panda.utils.PaginationResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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

    @Transactional
    @Override
    public void updateArticleReviewStatus(String articleId, Integer status) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);
        Article article = new Article();
        article.setArticleStatus(status);
        if (articleMapper.updateByExampleSelective(article, example) != 1) {
            EncapsulatedException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
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
