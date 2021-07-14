package com.panda.api.controller.article;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value = "portal article controller", tags = {"portal article controller"})
@RequestMapping("/api/service-article/article/portal")
public interface ArticlePortalControllerApi {
    @GetMapping("list")
    @ApiOperation(value = "portal list articles", notes = "portal list articles", httpMethod = "GET")
    public ResponseResult listArticleForUser(@RequestParam String keyword,
                                      @RequestParam Integer category,
                                      @RequestParam Integer page,
                                      @RequestParam Integer pageSize);

    @GetMapping("list/populars")
    @ApiOperation(value = "list popular articles", notes = "list popular articles", httpMethod = "GET")
    public ResponseResult listPopularArticles();
}
