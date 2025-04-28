package com.uon.KCBMobileApis.models;

import lombok.Data;

@Data
public class MpesaCallBack {
    private String status;
    private String statusDescription;
    private String requestId;
    private String mobileNumber;
    private String amount;
    private String transactionId;
    private String receiverName;
}
