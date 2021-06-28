package com.panda.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminLoginBO {
    @NotBlank(message = "username cannot be blank")
    private String username;
    @NotBlank(message = "password cannot be blank")
    private String password;
    private String img64;
}
