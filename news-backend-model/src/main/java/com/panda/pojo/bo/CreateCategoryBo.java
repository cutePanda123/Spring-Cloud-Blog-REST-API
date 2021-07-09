package com.panda.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateCategoryBo {
    private Integer id;
    @NotBlank(message = "category name cannot be blank")
    private String name;
    private String oldName;
    @NotBlank(message = "category color cannot be blank")
    private String tagColor;
}
