package com.uon.KCBMobileApis.helpers.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    private String phoneNumber;
    private String message;
    private String senderId;
    private String refId;
}
