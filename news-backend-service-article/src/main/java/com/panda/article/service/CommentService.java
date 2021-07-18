package com.panda.article.service;

import com.panda.utils.PaginationResult;

public interface CommentService {
    public void createComment(String articleId,
                              String parentCommentId,
                              String content,
                              String userId);

    public int getCommentCount(String articleId);

    public PaginationResult listComments(String articleId, Integer page, Integer pageSize);

    public PaginationResult listCommentsForWriter(String writerId, Integer page, Integer pageSize);

    public void deleteComment(String writerId, String commentId);
}
