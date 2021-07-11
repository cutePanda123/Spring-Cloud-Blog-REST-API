package com.panda.article.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.article.ArticleControllerApi;
import com.panda.article.service.ArticleService;
import com.panda.enums.ArticleCoverType;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.Category;
import com.panda.pojo.bo.CreateArticleBo;
import com.panda.utils.JsonUtils;
import com.panda.utils.PaginationResult;
import com.panda.utils.RedisAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {
    @Autowired
    private RedisAdaptor redisAdaptor;

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult createArticle(@Valid CreateArticleBo bo, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseResult.errorMap(getErrors(result));
        }
        if (bo.getArticleType() == ArticleCoverType.image.type && StringUtils.isBlank(bo.getArticleCover())) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
        }
        if (bo.getArticleType() == ArticleCoverType.text.type) {
            bo.setArticleCover("");
        }
        String categories = redisAdaptor.get(REDIS_ALL_CATEGORY_KEY);
        if (StringUtils.isBlank(categories)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        List<Category> categoryList = JsonUtils.jsonToList(categories, Category.class);
        Category category = categoryList
                .stream()
                .filter(item -> item.getId() == bo.getCategoryId())
                .findAny().orElse(null);
        if (category == null) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
        }
        articleService.createArticle(bo, category);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult listForUser(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PaginationResult result = articleService.listArticles(userId, keyword, status, startDate, endDate, page, pageSize);
        return ResponseResult.ok(result);
    }

    @Override
    public ResponseResult listForAdmin(Integer status, Integer page, Integer pageSize) {
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PaginationResult result = articleService.listArticlesWithStatus(status, page, pageSize);
        return ResponseResult.ok(result);
    }
}
