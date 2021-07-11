package com.panda.article.service;

import com.panda.pojo.Category;
import com.panda.pojo.bo.CreateArticleBo;
import com.panda.utils.PaginationResult;

import java.util.Date;

public interface ArticleService {
    public void createArticle(CreateArticleBo bo, Category category);

    public void updateScheduledArticleToAdhoc();

    public PaginationResult listArticles(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize);
}
