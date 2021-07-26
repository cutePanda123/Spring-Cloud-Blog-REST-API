package com.panda.article.task;

import com.panda.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

//@Configuration
//@EnableScheduling
// Implemented publish scheduled article with RabbitMq delayed message
// and deprecated this scheduled task because it does entire table scanning
// which leads to performance issue
public class PublishArticleTask {
    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0/3 * * * * ?")
    private void publishArticles() {
        //System.out.println("execute scheduled task to publish articles: " + LocalDateTime.now());
        //articleService.updateScheduledArticleToAdhoc();
    }
}
