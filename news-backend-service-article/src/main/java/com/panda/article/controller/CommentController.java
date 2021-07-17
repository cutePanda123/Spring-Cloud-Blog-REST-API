package com.panda.article.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.article.CommentControllerApi;
import com.panda.article.service.CommentService;
import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.CommentReplyBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
public class CommentController extends BaseController implements CommentControllerApi {
    @Autowired
    private CommentService commentService;

    @Override
    public ResponseResult createComment(@Valid CommentReplyBo bo, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return ResponseResult.errorMap(map);
        }
        commentService.createComment(bo.getArticleId(), bo.getParentId(), bo.getContent(), bo.getUserId());
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult getCommentCount(String articleId) {
        return ResponseResult.ok(commentService.getCommentCount(articleId));
    }
}
