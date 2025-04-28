package com.uon.KCBMobileApis.utils;

public enum IftTransactionActions {
    COMPLETED("001"),
    REJECTED("002"),
    PENDING("003");

    public final String value;

    IftTransactionActions(String value) {
        this.value = value;
    }
}
