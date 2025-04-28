package com.uon.KCBMobileApis.helpers.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsApiResponse {
    private String keyword;
    private String status;
    private String statusCode;
}
