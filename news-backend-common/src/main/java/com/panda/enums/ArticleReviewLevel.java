package com.panda.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ArticleReviewLevel {
    pass("pass", "automatic review passed"),
    blocked("blocked", "automatic review failed"),
    manualReview("manual", "need a manual review");

    public final String type;
    public final String value;
}
