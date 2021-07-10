package com.panda.article.service;

import com.panda.pojo.Category;
import com.panda.pojo.bo.CreateArticleBo;

public interface ArticleService {
    public void createArticle(CreateArticleBo bo, Category category);
}
