package com.panda.pojo.bo;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RelatedLinkBo {
    private String id;
    @NotBlank(message = "link name cannot be blank")
    private String linkName;
    @NotBlank(message = "link url cannot be blank")
    @URL(message = "link url cannot match a valid format")
    private String linkUrl;
    @NotNull(message = "isDelete flag cannot be null")
    private Integer isDelete;
}
