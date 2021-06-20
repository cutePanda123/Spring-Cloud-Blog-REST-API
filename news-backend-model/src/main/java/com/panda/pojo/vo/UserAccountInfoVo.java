package com.panda.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserAccountInfoVo {
    private String id;
    private String mobile;
    private String nickname;
    private String face;
    private String realname;
    private String email;
    private Integer sex;
    private Date birthday;
    private String province;
    private String city;
    private String district;
}