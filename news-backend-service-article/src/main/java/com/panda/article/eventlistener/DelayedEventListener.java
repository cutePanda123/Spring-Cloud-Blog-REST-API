package com.panda.article.eventlistener;

import com.panda.api.config.RabbitDelayedMqConfig;
import com.panda.article.service.ArticleService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DelayedEventListener {
    @Autowired
    private ArticleService articleService;

    @RabbitListener(queues = {RabbitDelayedMqConfig.DELAYED_QUEUE_NAME})
    public void listenToPublishDelayedArticleEvent(String payload, Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if (!routingKey.equalsIgnoreCase(RabbitDelayedMqConfig.DELAYED_PUBLISH_ARTICLE_ROUTING_KEY)) {
            return;
        }
        String articleId = payload;
        articleService.publishArticle(articleId);
    }

}
