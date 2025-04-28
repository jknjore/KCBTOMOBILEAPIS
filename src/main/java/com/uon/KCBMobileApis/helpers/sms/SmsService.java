package com.uon.KCBMobileApis.helpers.sms;

import com.uon.KCBMobileApis.helpers.PhoneFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class SmsService {
    @Value("${endpoint.smsURL}")
    private String smsUrl;

    private final RestTemplate restTemplate;

    public SmsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SmsResult sendSms(String phoneNumber, String message)
    {
        return sendSms( phoneNumber,  message, "KCB", LocalDateTime.now().toString());
    }

    public SmsResult sendSms(String phoneNumber, String message, String senderId, String refId) {
        // Create request payload
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setPhoneNumber(new PhoneFormatter().numberCheck(phoneNumber));
        smsRequest.setMessage(message);
        smsRequest.setSenderId(senderId);
        smsRequest.setRefId(refId);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SmsRequest> request = new HttpEntity<>(smsRequest, headers);

        try {
            // Make API call
            ResponseEntity<SmsApiResponse> response = restTemplate.postForEntity(
                    smsUrl,
                    request,
                    SmsApiResponse.class
            );

            SmsApiResponse apiResponse = response.getBody();
            if (apiResponse != null) {
                String statusCode = apiResponse.getStatusCode();
                String statusMessage = apiResponse.getStatus();
                String resultMessage = statusCode + " - " + statusMessage;

                // Check if SMS was sent successfully
                if ("0".equals(statusCode)) {
                    return new SmsResult(true, resultMessage);
                } else {
                    return new SmsResult(false, resultMessage);
                }
            } else {
                return new SmsResult(false, "No response from SMS API");
            }
        } catch (Exception e) {
            return new SmsResult(false, "Error calling SMS API: " + e.getMessage());
        }
    }
}
