package com.panda.pojo.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString
public class CreateArticleBo {

    @NotBlank(message = "title should not be blank")
    @Length(max = 30, message = "title should be less than 30 characters")
    private String title;

    @NotBlank(message = "content should not be blank")
    @Length(max = 9999, message = "content should be less than 10000 characters")
    private String content;

    @NotNull(message = "category cannot be blank")
    private Integer categoryId;

    @NotNull(message = "type cannot be blank")
    @Min(value = 1, message = "min type option")
    @Max(value = 2, message = "max type option")
    private Integer articleType;

    private String articleCover;

    @NotNull(message = "publish method cannot be blank")
    @Min(value = 0, message = "scheduled publish")
    @Max(value = 1, message = "ad-hoc publish")
    private Integer isAppoint;

    // date string from client is converted to date object in server
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @NotBlank(message = "user id cannot be blank")
    private String publishUserId;
}