package com.panda.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.panda.api.service.BaseService;
import com.panda.article.mapper.ArticleMapper;
import com.panda.article.service.ArticlePortalService;
import com.panda.enums.ArticleReviewStatus;
import com.panda.enums.YesNoType;
import com.panda.json.result.ResponseResult;
import com.panda.pojo.Article;
import com.panda.pojo.vo.AppUserVo;
import com.panda.pojo.vo.ArticleDetailVo;
import com.panda.pojo.vo.ArticleVo;
import com.panda.utils.JsonUtils;
import com.panda.utils.PaginationResult;
import com.panda.utils.RedisAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class ArticlePortalServiceImpl extends BaseService implements ArticlePortalService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisAdaptor redisAdaptor;
    private final static String userServiceListUserApiUrl =
            "http://user.news.com:8003/api/service-user/user/list";

    @Override
    public PaginationResult listArticles(String keyword, Integer category, Integer page, Integer pageSize) {
        Example example = buildDefaultExample();
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (category != null) {
            criteria.andEqualTo("categoryId", category);
        }
        PageHelper.startPage(page, pageSize);
        List<Article> articles = articleMapper.selectByExample(example);
        Set<String> publisherIds = new HashSet<>();
        articles.forEach(article -> publisherIds.add(article.getPublishUserId()));
        List<AppUserVo> userVoList = listPublishers(publisherIds);

        List<ArticleVo> articleVoList = new LinkedList<>();
        for (Article article : articles) {
            ArticleVo vo = new ArticleVo();
            BeanUtils.copyProperties(article, vo);
            vo.setPublisherVO(getArticlePublisher(article.getPublishUserId(), userVoList));
            articleVoList.add(vo);
        }

        return paginationResultBuilder(articleVoList, page);
    }

    @Override
    public List<ArticleVo> listPopularArticles() {
        Example example = buildDefaultExample();
        PageHelper.startPage(1, 5);
        List<Article> articleList = articleMapper.selectByExample(example);
        List<ArticleVo> articleVoList = new LinkedList<>();
        articleList.forEach(article -> {
            ArticleVo vo = new ArticleVo();
            BeanUtils.copyProperties(article, vo);
            articleVoList.add(vo);
        });
        return articleVoList;
    }

    @Override
    public ArticleDetailVo getArticle(String articleId) {
        Article article = new Article();
        article.setId(articleId);
        article.setIsDelete(YesNoType.no.type);
        article.setIsAppoint(YesNoType.no.type);
        article.setArticleStatus(ArticleReviewStatus.success.type);
        Article result = articleMapper.selectOne(article);
        ArticleDetailVo vo = new ArticleDetailVo();
        BeanUtils.copyProperties(result, vo);
        Set<String> idSet = new HashSet<>();
        idSet.add(result.getPublishUserId());
        List<AppUserVo> publisherList = listPublishers(idSet);
        if (!publisherList.isEmpty()) {
            vo.setPublishUserName(publisherList.get(0).getNickname());
        }
        vo.setReadCount(redisAdaptor.get(
                REDIS_ARTICLE_READ_COUNTS_PREFIX + ":" + articleId
        ));
        return vo;
    }

    @Override
    public void incrementArticleReadCount(String articleId) {
        redisAdaptor.increment(
                REDIS_ARTICLE_READ_COUNTS_PREFIX + ":" + articleId,
                1
        );
    }

    private AppUserVo getArticlePublisher(String publisherId, List<AppUserVo> userVoList) {
        for (AppUserVo userVo : userVoList) {
            if (userVo.getId().equalsIgnoreCase(publisherId)) {
                return userVo;
            }
        }
        return null;
    }

    private Example buildDefaultExample() {
        Example example = new Example(Article.class);
        example.orderBy("publishTime").desc();
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isAppoint", YesNoType.no.type);
        criteria.andEqualTo("isDelete", YesNoType.no.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.success.type);
        return example;
    }

    private List<AppUserVo> listPublishers(Set<String> publisherIds) {
        RequestEntity<Void> requestEntity = null;
        try {
            requestEntity =
                    RequestEntity.
                            get(new URI(ArticlePortalServiceImpl.userServiceListUserApiUrl)).
                            accept(MediaType.APPLICATION_JSON).
                            header("userIds", JsonUtils.objectToJson(publisherIds)).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ResponseEntity<ResponseResult> responseEntity = restTemplate.exchange(requestEntity, ResponseResult.class);
        List<AppUserVo> userVoList = new LinkedList<>();
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String users = JsonUtils.objectToJson(responseEntity.getBody().getData());
            userVoList = JsonUtils.jsonToList(users, AppUserVo.class);
        }
        return userVoList;
    }
}
