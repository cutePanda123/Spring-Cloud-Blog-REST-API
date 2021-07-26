package com.panda.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitDelayedMqConfig {
    public static final String DELAYED_EXCHANGE_NAME = "delayed_exchange";
    public static final String DELAYED_QUEUE_NAME = "delayed_queue";
    public static final String DELAYED_MESSAGE_ROUTING_KEY = "publish.delayed.#";
    public static final String DELAYED_PUBLISH_ARTICLE_ROUTING_KEY = "publish.delayed.article";

    @Bean(DELAYED_EXCHANGE_NAME)
    public Exchange exchange() {
        return ExchangeBuilder
                .topicExchange(DELAYED_EXCHANGE_NAME)
                .delayed()
                .durable(true)
                .build();
    }

    @Bean(DELAYED_QUEUE_NAME)
    public Queue queue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    @Bean
    public Binding bindDelayedMq(
            @Qualifier(DELAYED_EXCHANGE_NAME) Exchange exchange,
            @Qualifier(DELAYED_QUEUE_NAME) Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(DELAYED_MESSAGE_ROUTING_KEY)
                .noargs();
    }
}
