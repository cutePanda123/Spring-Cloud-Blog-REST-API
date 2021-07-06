package com.panda.fs.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GridFsConfig {
    @Value("${spring.data.mongodb.uri}")
    private String connectionUri;

    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase(connectionUri);
        GridFSBucket bucket = GridFSBuckets.create(database);
        return bucket;
    }
}
