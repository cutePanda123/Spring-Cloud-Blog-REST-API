package com.panda.api.controller.article;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value = "portal article controller", tags = {"portal article controller"})
@RequestMapping("/api/service-article/article/portal")
public interface ArticlePortalControllerApi {
    @GetMapping("list")
    @ApiOperation(value = "portal list articles", notes = "portal list articles", httpMethod = "GET")
    public ResponseResult listArticleForUser(@RequestParam String keyword,
                                      @RequestParam Integer category,
                                      @RequestParam Integer page,
                                      @RequestParam Integer pageSize);

    @GetMapping("search")
    @ApiOperation(value = "list articles for es", notes = "list articles for es", httpMethod = "GET")
    public ResponseResult searchArticles(@RequestParam String keyword,
                                             @RequestParam Integer category,
                                             @RequestParam Integer page,
                                             @RequestParam Integer pageSize);

    @GetMapping("list/populars")
    @ApiOperation(value = "list popular articles", notes = "list popular articles", httpMethod = "GET")
    public ResponseResult listPopularArticles();

    @GetMapping("get")
    @ApiOperation(value = "get article", notes = "get article", httpMethod = "GET")
    public ResponseResult getArticle(@RequestParam String articleId);

    @PostMapping("readcount")
    @ApiOperation(
            value = "increment article read count",
            notes = "increment article read count",
            httpMethod = "POST")
    public ResponseResult incrementReadCount(
            @RequestParam String articleId,
            HttpServletRequest request);

    @GetMapping("articlereadcount")
    @ApiOperation(value = "get article read count", notes = "get article read count", httpMethod = "GET")
    public ResponseResult getArticleReadCount(@RequestParam String articleId);
}
