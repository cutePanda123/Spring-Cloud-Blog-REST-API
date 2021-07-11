package com.panda.article.mapper;

import com.panda.my.mapper.MyMapper;
import com.panda.pojo.Article;

public interface ArticleMapperCustom extends MyMapper<Article> {
    public void updateScheduledArticleToAdhoc();
}