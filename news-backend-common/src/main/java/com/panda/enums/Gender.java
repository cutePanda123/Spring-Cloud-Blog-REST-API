package com.panda.enums;

public enum Gender {
    female(0, "female"),
    male(1, "male"),
    unknown(2, "unknown");

    public final Integer type;
    public final String value;

    Gender(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}

