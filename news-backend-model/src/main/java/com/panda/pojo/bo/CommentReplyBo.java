package com.panda.pojo.bo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class CommentReplyBo {
    @NotBlank(message = "article id cannot be blank")
    private String articleId;
    @NotBlank(message = "parent comment id cannot be blank")
    private String parentId;
    @NotBlank(message = "user id cannot be blank")
    private String userId;
    @NotBlank(message = "article id cannot be blank")
    @Length(max = 50, message = "content cannot be longer than 50 characters")
    private String content;
}
