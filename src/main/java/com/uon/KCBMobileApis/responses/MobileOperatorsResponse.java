package com.uon.KCBMobileApis.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MobileOperatorsResponse {
    private String status;
    private String message;
    private String dbpTransactionNumber;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal chargesAmount;
    private String uniqueTransactionReference;
}
