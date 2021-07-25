package com.panda.article.generator.service;

import org.springframework.amqp.core.Message;

public interface GeneratedArticleEventHandlerService {
    public void downloadGeneratedArticle(String articleId, String gridFsId);
}
