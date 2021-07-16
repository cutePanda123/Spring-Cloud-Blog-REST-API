package com.panda.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleDetailVo {
    private String id;
    private String title;
    private String cover;
    private Integer categoryId;
    private String categoryName;
    private String publishUserId;
    private Date publishTime;
    private String content;
    private String publishUserName;
}
