package com.panda.api.controller.article;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "generated article page controller", tags = {"generated article page controller"})
@RequestMapping("/api/service-article/article/html")
public interface ArticleGeneratedPageApi {
    @GetMapping("get")
    @ApiOperation(value = "get generated page", notes = "get generated page", httpMethod = "GET")
    public ResponseResult getArticlePage(@RequestParam String articleId, @RequestParam String gridFsId);
}
