package com.panda.article.service;

import com.panda.utils.PaginationResult;

public interface ArticlePortalService {
    public PaginationResult listArticles(String keyword, Integer category, Integer page, Integer pageSize);
}
