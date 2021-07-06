package com.panda.pojo.mo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RelatedLinkMo implements Serializable {
    private String id;
    private String linkName;
    private String linkUrl;
    private Integer isDelete;
    private Date createTime;
    private Date updateTime;
}
