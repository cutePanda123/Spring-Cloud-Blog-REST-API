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
    public ResponseResult listForUser(@RequestParam String userId,
                               @RequestParam String keyword,
                               @RequestParam Integer status,
                               @RequestParam Date startDate,
                               @RequestParam Date endDate,
                               @RequestParam Integer page,
                               @RequestParam Integer pageSize);

    @PostMapping("admin/list")
    @ApiOperation(value = "list articles for admin", notes = "list articles for admin", httpMethod = "POST")
    public ResponseResult listForAdmin(
                               @RequestParam Integer status,
                               @RequestParam Integer page,
                               @RequestParam Integer pageSize);

    @PostMapping("admin/review")
    @ApiOperation(value = "review article", notes = "review article", httpMethod = "POST")
    public ResponseResult reviewArticle(
            @RequestParam String articleId,
            @RequestParam Integer isPassed);

    @PostMapping("user/delete")
    @ApiOperation(value = "user delete article", notes = "user delete article", httpMethod = "POST")
    public ResponseResult deleteArticle(
            @RequestParam String userId,
            @RequestParam String articleId);

    @PostMapping("user/withdraw")
    @ApiOperation(value = "user withdraw article", notes = "user withdraw article", httpMethod = "POST")
    public ResponseResult withdrawArticle(
            @RequestParam String userId,
            @RequestParam String articleId);
}
