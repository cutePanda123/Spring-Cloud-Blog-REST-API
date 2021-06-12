package com.panda.enums;

public enum UserAccountStatus {
    INACTIVE(0, "inactive"),
    ACTIVE(1, "active"),
    FROZEN(2, "frozen");

    public final Integer type;
    public final String value;

    UserAccountStatus(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Determine whether the incoming user status is a valid value
     * @param tempStatus
     * @return
     */
    public static boolean isUserStatusValid(Integer tempStatus) {
        if (tempStatus != null) {
            if (tempStatus == INACTIVE.type || tempStatus == ACTIVE.type || tempStatus == FROZEN.type) {
                return true;
            }
        }
        return false;
    }
}
