package com.panda.api.controller.article;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.CommentReplyBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(value = "comment controller", tags = {"comment controller"})
@RequestMapping("/api/service-article/comment")
public interface CommentControllerApi {
    @PostMapping("/create")
    @ApiOperation(value = "create comment", notes = "create comment", httpMethod = "POST")
    public ResponseResult createComment(@RequestBody @Valid CommentReplyBo bo, BindingResult result);
}
