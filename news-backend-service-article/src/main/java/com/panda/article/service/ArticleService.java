package com.panda.article.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.panda.pojo.Category;
import com.panda.pojo.bo.CreateArticleBo;
import com.panda.utils.PaginationResult;

import java.io.IOException;
import java.util.Date;

public interface ArticleService {
    public void createArticle(CreateArticleBo bo, Category category);

    public void updateScheduledArticleToAdhoc();

    public PaginationResult listArticles(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize);

    public PaginationResult listArticlesWithStatus(Integer status, Integer page, Integer pageSize);

    public void updateArticleReviewStatus(String articleId, Integer status) throws IOException;

    public void deleteArticle(String userId, String articleId) throws IOException;

    public void withdrawArticle(String userId, String articleId) throws IOException;

    public void saveArticleGridFsFileId(String articleId, String fileId);

    public void publishArticle(String articleId);
}
