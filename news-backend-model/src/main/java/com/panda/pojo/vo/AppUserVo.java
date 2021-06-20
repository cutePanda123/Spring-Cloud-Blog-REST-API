package com.panda.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AppUserVo {
    private String id;
    private Integer activeStatus;
    private String nickname;
    private String face;
}