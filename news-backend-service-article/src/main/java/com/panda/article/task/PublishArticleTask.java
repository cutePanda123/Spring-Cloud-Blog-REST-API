package com.panda.article.task;

import com.panda.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class PublishArticleTask {
    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0/3 * * * * ?")
    private void publishArticles() {
        //System.out.println("execute scheduled task to publish articles: " + LocalDateTime.now());
        //articleService.updateScheduledArticleToAdhoc();
    }
}
