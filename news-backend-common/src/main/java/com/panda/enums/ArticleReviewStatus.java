package com.panda.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ArticleReviewStatus {
    reviewing(1, "automatic review"),
    waitingManual(2, "automatic review finished, waiting for manual review"),
    success(3, "passed"),
    failed(4, "failed"),
    withdraw(5, "rejected");

    public final Integer type;
    public final String value;

    public static boolean isValidStatus(Integer status) {
        return status != null && (status == reviewing.type ||
                status == waitingManual.type || status == success.type ||
                status == failed.type || status == withdraw.type);
    }
}
