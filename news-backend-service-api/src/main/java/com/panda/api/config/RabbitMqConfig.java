package com.panda.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String GENERATED_ARTICLE_EXCHANGE_NAME = "generated_article_exchange";
    public static final String GENERATED_ARTICLE_QUEUE_NAME = "generated_article_queue";
    public static final String GENERATED_ARTICLE_ROUTING_KEY = "article.generated.do";

    @Bean(GENERATED_ARTICLE_EXCHANGE_NAME)
    public Exchange exchange() {
        return ExchangeBuilder
                .topicExchange(GENERATED_ARTICLE_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    @Bean(GENERATED_ARTICLE_QUEUE_NAME)
    public Queue queue() {
        return new Queue(GENERATED_ARTICLE_QUEUE_NAME);
    }

    @Bean
    public Binding bind(
            @Qualifier(GENERATED_ARTICLE_EXCHANGE_NAME) Exchange exchange,
            @Qualifier(GENERATED_ARTICLE_QUEUE_NAME) Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("article.#.do")
                .noargs();
    }
}
