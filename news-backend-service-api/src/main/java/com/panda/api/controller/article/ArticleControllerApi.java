package com.panda.api.controller.article;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.CreateArticleBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

@Api(value = "article controller", tags = {"article controller"})
@RequestMapping("/api/service-article/article")
public interface ArticleControllerApi {
    @PostMapping("createArticle")
    @ApiOperation(value = "publish article", notes = "publish article", httpMethod = "POST")
    public ResponseResult createArticle(@RequestBody @Valid CreateArticleBo bo, BindingResult result);

    @PostMapping("list")
    @ApiOperation(value = "list articles", notes = "list articles", httpMethod = "POST")
    public ResponseResult list(@RequestParam String userId,
                               @RequestParam String keyword,
                               @RequestParam Integer status,
                               @RequestParam Date startDate,
                               @RequestParam Date endDate,
                               @RequestParam Integer page,
                               @RequestParam Integer pageSize);
}
