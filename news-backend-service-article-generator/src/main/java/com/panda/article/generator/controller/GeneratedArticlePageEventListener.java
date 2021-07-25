package com.panda.article.generator.controller;

import com.panda.api.config.RabbitMqConfig;
import com.panda.article.generator.service.GeneratedArticleEventHandlerService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneratedArticlePageEventListener {
    @Autowired
    private GeneratedArticleEventHandlerService handlerService;

    @RabbitListener(queues = {RabbitMqConfig.GENERATED_ARTICLE_QUEUE_NAME})
    public void listenToGeneratedArticleEvent(String payload, Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if (!routingKey.equalsIgnoreCase(RabbitMqConfig.GENERATED_ARTICLE_ROUTING_KEY)) {
            return;
        }
        String articleId = payload.split(",")[0];
        String gridFsFileId = payload.split(",")[1];
        handlerService.downloadGeneratedArticle(articleId, gridFsFileId);
    }

}
