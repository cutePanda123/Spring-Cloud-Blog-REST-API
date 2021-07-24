package com.panda.api.controller.article;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(
        value = "client controller to download article page from GridFs",
        tags = {"client controller to download article page from GridFs"})
@RequestMapping("/api/service-static/article")
public interface ArticleGeneratedPageApi {
    @GetMapping("get")
    @ApiOperation(
            value = "notice client server to download article page from GridFs",
            notes = "notice client server to download article page from GridFs",
            httpMethod = "GET")
    public ResponseResult downloadArticlePage(@RequestParam String articleId, @RequestParam String gridFsId);
}
