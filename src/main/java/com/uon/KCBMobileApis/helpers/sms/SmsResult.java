package com.uon.KCBMobileApis.helpers.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsResult {
    private boolean smsSent;
    private String message;
}
