package com.panda.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewAdminBO {
    private String username;
    private String adminName;
    private String password;
    private String confirmedPassword;
    private String img64;
    private String faceId;
}
