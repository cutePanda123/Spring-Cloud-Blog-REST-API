package com.panda.article.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.article.ArticlePortalControllerApi;
import com.panda.article.service.ArticlePortalService;
import com.panda.article.service.ArticleService;
import com.panda.json.result.ResponseResult;
import com.panda.utils.PaginationResult;
import com.panda.utils.RedisAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {
    @Autowired
    private RedisAdaptor redisAdaptor;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticlePortalService articlePortalService;

    @Override
    public ResponseResult listForUser(String keyword, Integer category, Integer page, Integer pageSize) {
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PaginationResult result = articlePortalService.listArticles(keyword, category, page, pageSize);
        return ResponseResult.ok(result);
    }
}
