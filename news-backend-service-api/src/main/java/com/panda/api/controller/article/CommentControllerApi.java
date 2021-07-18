package com.panda.api.controller.article;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.CommentReplyBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "comment controller", tags = {"comment controller"})
@RequestMapping("/api/service-article/comment")
public interface CommentControllerApi {
    @PostMapping("/create")
    @ApiOperation(value = "create comment", notes = "create comment", httpMethod = "POST")
    public ResponseResult createComment(@RequestBody @Valid CommentReplyBo bo, BindingResult result);

    @GetMapping("/count")
    @ApiOperation(value = "get comment count", notes = "get comment count", httpMethod = "GET")
    public ResponseResult getCommentCount(String articleId);

    @GetMapping("/list")
    @ApiOperation(value = "list comments", notes = "list comments", httpMethod = "GET")
    public ResponseResult listComments(
            @RequestParam String articleId,
            @RequestParam Integer page,
            @RequestParam Integer pageSize);

    @PostMapping("/list")
    @ApiOperation(value = "list comments with writer id", notes = "list comments with writer id", httpMethod = "POST")
    public ResponseResult listCommentsForWriter(
            @RequestParam String writerId,
            @RequestParam Integer page,
            @RequestParam Integer pageSize);

    @PostMapping("/delete")
    @ApiOperation(value = "delete a comment", notes = "delete a comment", httpMethod = "POST")
    public ResponseResult deleteComment(
            @RequestParam String writerId,
            @RequestParam String commentId
    );
}
