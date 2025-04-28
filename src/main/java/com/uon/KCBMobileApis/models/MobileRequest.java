package com.uon.KCBMobileApis.models;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class MobileRequest {
    private String dbpTransactionReference;
    private String debitAccountID;
    private String debitAccountMobile;
    private String creditAccountMobile;
    private String creditAccountName;
    private String mobileMoneyOperatorName;
    private BigDecimal amount;
}