package com.panda.article.service.impl;

import com.panda.article.mapper.ArticleMapper;
import com.panda.article.service.ArticleService;
import com.panda.enums.ArticlePublishType;
import com.panda.enums.ArticleReviewStatus;
import com.panda.enums.YesNoType;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.Article;
import com.panda.pojo.Category;
import com.panda.pojo.bo.CreateArticleBo;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    Sid sid;

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
}
