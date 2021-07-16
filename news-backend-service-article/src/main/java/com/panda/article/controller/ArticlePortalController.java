package com.panda.article.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.article.ArticlePortalControllerApi;
import com.panda.article.service.ArticlePortalService;
import com.panda.article.service.ArticleService;
import com.panda.json.result.ResponseResult;
import com.panda.pojo.vo.ArticleDetailVo;
import com.panda.utils.IPUtils;
import com.panda.utils.PaginationResult;
import com.panda.utils.RedisAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseResult listArticleForUser(String keyword, Integer category, Integer page, Integer pageSize) {
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PaginationResult result = articlePortalService.listArticles(keyword, category, page, pageSize);
        return ResponseResult.ok(result);
    }

    @Override
    public ResponseResult listPopularArticles() {
        return ResponseResult.ok(articlePortalService.listPopularArticles());
    }

    @Override
    public ResponseResult getArticle(String articleId) {
        ArticleDetailVo vo = articlePortalService.getArticle(articleId);
        return ResponseResult.ok(vo);
    }

    @Override
    public ResponseResult incrementReadCount(String articleId, HttpServletRequest request) {
        String ip = IPUtils.getRequestIp(request);
        redisAdaptor.setnx(REDIS_ARTICLE_ALREADY_READ_PREFIX + ":" + articleId + ":" + ip, ip);
        articlePortalService.incrementArticleReadCount(articleId);
        return ResponseResult.ok();
    }
}
