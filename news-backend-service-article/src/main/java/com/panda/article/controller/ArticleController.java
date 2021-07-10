package com.panda.article.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.article.ArticleControllerApi;
import com.panda.enums.ArticleCoverType;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.Category;
import com.panda.pojo.bo.CreateArticleBo;
import com.panda.utils.JsonUtils;
import com.panda.utils.RedisAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {
    @Autowired
    private RedisAdaptor redisAdaptor;

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
        Category target = categoryList
                .stream()
                .filter(category -> category.getId() == bo.getCategoryId())
                .findAny().orElse(null);
        if (target == null) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
        }
        return null;
    }
}
