package com.panda.api.controller.article;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.CreateArticleBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(value = "article controller", tags = {"article controller"})
@RequestMapping("/api/service-article/article")
public interface ArticleControllerApi {
    @PostMapping("createArticle")
    @ApiOperation(value = "publish article", notes = "publish article", httpMethod = "PSOT")
    public ResponseResult createArticle(@RequestBody @Valid CreateArticleBo bo, BindingResult result);
}
