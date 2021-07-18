package com.panda.pojo.vo;

import lombok.Data;

@Data
public class CommentVo {
    private String commentId;
    private String parentId;
    private String articleId;
    private String commentUserId;
    private String commentUserNickName;
    private String content;
    private String createTime;
    private String quoteUserNickname;
    private String quoteContent;
}
