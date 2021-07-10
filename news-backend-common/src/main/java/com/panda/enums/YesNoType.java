package com.panda.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum YesNoType {
    yes(1, "yes"),
    no(0, "no");

    public final Integer type;
    public final String value;
}
