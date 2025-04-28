package com.uon.KCBMobileApis.tasks;


import com.uon.KCBMobileApis.entities.SMSToCustomers;
import com.uon.KCBMobileApis.helpers.sms.SmsResult;
import com.uon.KCBMobileApis.helpers.sms.SmsService;
import com.uon.KCBMobileApis.repositories.SMSToCustomersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SmsRetryScheduler {

    @Autowired
    private SMSToCustomersRepo smsToCustomersRepo;

    @Autowired
    private SmsService smsService;

    @Value("${config.maxSmsSendRetries}")
    private int maxSmsSendRetries;

    @Value("${config.minSmsRetryIntervalMins}")
    private int minSmsRetryIntervalMins;

    @Value("${config.maxSmsRetryLifespanMins}")
    private int maxSmsRetryLifespanMins;

    @Scheduled(fixedDelayString = "${config.smsRetryIterationMins}", timeUnit = TimeUnit.MINUTES)

    @Transactional
    public void retryFailedSms() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxCreationTime = now.minus(maxSmsRetryLifespanMins, ChronoUnit.MINUTES);
        LocalDateTime minRetryTime = now.minus(minSmsRetryIntervalMins, ChronoUnit.MINUTES);

        // Fetch records that meet all criteria
        List<SMSToCustomers> failedSmsList = smsToCustomersRepo.findFailedSmsForRetry(
                maxSmsSendRetries,
                maxCreationTime,
                minRetryTime
        );

        System.out.println("Sending " + failedSmsList.size() + " records of failed sms at " + LocalDateTime.now());

        for (SMSToCustomers sms : failedSmsList) {
            try {
                SmsResult smsResult = smsService.sendSms(
                        sms.getPhoneNumber(),
                        sms.getMessage(),
                        "KCB",
                        sms.getDbpTransactionReference()
                );

                // Update the record
                sms.setSent(smsResult.isSmsSent());
                sms.setDateRetried(LocalDateTime.now());
                sms.setSendRetries(sms.getSendRetries() + 1);

                if (!smsResult.isSmsSent()) {
                    sms.setFailReason(smsResult.getMessage());
                }

                smsToCustomersRepo.save(sms);
            } catch (Exception e) {
                // Log the error and continue with next record
                sms.setFailReason("System error: " + e.getMessage());
                sms.setDateRetried(LocalDateTime.now());
                sms.setSendRetries(sms.getSendRetries() + 1);
                smsToCustomersRepo.save(sms);
            }
        }
    }
}