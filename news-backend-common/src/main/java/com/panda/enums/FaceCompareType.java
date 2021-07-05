package com.panda.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FaceCompareType {
    base64(1, "compare image base 64 code"),
    imageUrl(0, "compare image from an url");

    public final Integer type;
    public final String value;

}
