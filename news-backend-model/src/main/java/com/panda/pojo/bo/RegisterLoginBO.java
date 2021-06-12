package com.panda.pojo.bo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class RegisterLoginBO {
    @NotBlank(message = "mobile cannot be null")
    private String mobile;
    @NotBlank(message = "sms code cannot be null")
    private String smsCode;
}
