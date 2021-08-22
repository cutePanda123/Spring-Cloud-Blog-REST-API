package com.panda.pojo.eo;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

@Data
@ToString
@Document(indexName = "articles", type = "_doc")
public class ArticleEO {
    @Id
    private String id;
    @Field
    private String title;
    @Field
    private Integer categoryId;
    @Field
    private Integer articleType;
    @Field
    private String articleCover;
    @Field
    private String publishUserId;
    @Field
    private Date publishTime;
}
