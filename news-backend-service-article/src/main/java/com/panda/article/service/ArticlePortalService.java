package com.panda.article.service;

import com.panda.pojo.vo.ArticleDetailVo;
import com.panda.pojo.vo.ArticleVo;
import com.panda.utils.PaginationResult;

import java.util.List;

public interface ArticlePortalService {
    public PaginationResult listArticles(String keyword, Integer category, Integer page, Integer pageSize);

    public List<ArticleVo> listPopularArticles();

    public ArticleDetailVo getArticle(String articleId);

    public void incrementArticleReadCount(String articleId);

    public Integer getArticleReadCount(String articleId);
}
