package com.panda.admin.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MongoClientConfig {
    @Value("${spring.data.mongodb.uri}")
    private String connectionUri;

    public final static String MONGODB_NAME = "news";
    public final static String RELATED_LINKS_COLLECTION_NAME = "relatedLinks";

    @Bean
    public MongoClient mongoClient() {
        MongoClient mongoClient = MongoClients.create(connectionUri);
        return mongoClient;
    }
}