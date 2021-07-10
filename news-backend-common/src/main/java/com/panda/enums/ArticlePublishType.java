package com.panda.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ArticlePublishType {
    scheduled(1, "publish at scheduled time"),
    adhoc(0, "publish immediately");

    public final Integer type;
    public final String value;
}
