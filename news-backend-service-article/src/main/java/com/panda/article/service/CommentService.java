package com.panda.article.service;

public interface CommentService {
    public void createComment(String articleId,
                              String parentCommentId,
                              String content,
                              String userId);

    public int getCommentCount(String articleId);
}
