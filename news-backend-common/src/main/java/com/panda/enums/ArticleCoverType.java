package com.panda.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ArticleCoverType {
    image(1, "image"),
    text(2, "text");

    public final Integer type;
    public final String value;
}
