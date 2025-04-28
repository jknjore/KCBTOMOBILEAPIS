package com.uon.KCBMobileApis.services;

import com.uon.KCBMobileApis.entities.BankToMobileTransferRequest;
import com.uon.KCBMobileApis.entities.SMSToCustomers;
import com.uon.KCBMobileApis.helpers.DateFormatter;
import com.uon.KCBMobileApis.helpers.sms.SmsResult;
import com.uon.KCBMobileApis.helpers.sms.SmsService;
import com.uon.KCBMobileApis.models.MobileRequest;
import com.uon.KCBMobileApis.models.MpesaCallBack;
import com.uon.KCBMobileApis.repositories.IbCallbackRepo;
import com.uon.KCBMobileApis.repositories.SMSToCustomersRepo;
import com.uon.KCBMobileApis.responses.MobileOperatorsResponse;
import com.uon.KCBMobileApis.services.interfaces.MobileProcessor;
import com.uon.KCBMobileApis.utils.IftTransactionActions;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MobileProcessorService implements MobileProcessor {
    private static final Logger LOG = LogManager.getLogger(MobileProcessorService.class);
    @Autowired
    MpesaProcessorService mpesaProcessor;
    @Autowired
    AirtelProcessorService airtelProcessor;
    @Autowired
    IbCallbackRepo ibCallbackRepo;
    @Autowired
    SMSToCustomersRepo smsToCustomersRepo;
    @Autowired
    SmsService smsService;
    public MobileOperatorsResponse mobileProcess(@Valid MobileRequest mobileRequest){
        MobileOperatorsResponse response = null;
        switch (mobileRequest.getMobileMoneyOperatorName()) {
            case "MPESA" -> {
                    response = mpesaProcessor.processMpesa(mobileRequest);
            }
            case "AIRTEL" -> {

                    response = airtelProcessor.processAirtel(mobileRequest);

                    String smsToCustomer = response.getStatus().equals(IftTransactionActions.COMPLETED.value) ? "Transaction " + response.getUniqueTransactionReference() + " successful. KES " + response.getCreditAmount() + " has been credited to M-PESA " + mobileRequest.getCreditAccountMobile() + " on " + new DateFormatter().getCurrentDateTime() : "Transaction for amount " + mobileRequest.getAmount() + " on " + new DateFormatter().getCurrentDateTime(LocalDateTime.now()) + " was unsuccessful. Please try again later";
                    //send message to customer
                    SmsResult smsResult = smsService.sendSms(mobileRequest.getDebitAccountMobile(), smsToCustomer, "KCB", mobileRequest.getDbpTransactionReference());
                    //log to SMS_TO_CUSTOMERS table
                    SMSToCustomers smsToCustomers = new SMSToCustomers(mobileRequest.getDbpTransactionReference(), LocalDateTime.now(), mobileRequest.getCreditAccountMobile(), smsToCustomer, "AIRTEL", smsResult.isSmsSent());
                    smsToCustomersRepo.save(smsToCustomers);

            }

        }
        return response;
    }


    public void callback(MpesaCallBack callback){
        Optional<BankToMobileTransferRequest> trans = ibCallbackRepo.findByTransactionRef(callback.getRequestId());
        if(trans.isPresent()){
            // Find the index of the hyphen
            int hyphenIndex = callback.getReceiverName().indexOf("-");
            if (hyphenIndex != -1) {
                // Extract everything after the hyphen and trim whitespace
                trans.get().setCreditAccountName(callback.getReceiverName().substring(hyphenIndex + 1).trim());
            }

            //Add try catch for sms to prevent possible interference with normal processing
            try {
                String smsToCustomer = callback.getStatus().equals("200") ? "Transaction " + callback.getTransactionId() + " successful. KES " + callback.getAmount() + " has been credited to M-PESA " + callback.getMobileNumber() + " on " + new DateFormatter().getCurrentDateTime() : "Transaction for amount " + trans.get().getAmount() + " on " + new DateFormatter().getCurrentDateTime(trans.get().getDateCreated()) + " was unsuccessful. Please try again later";
                //send message to customer
                SmsResult smsResult = smsService.sendSms(trans.get().getCreditAccountID(), smsToCustomer, "KCB", trans.get().getDbpTransactionReference());
                //log to SMS_TO_CUSTOMERS table
                SMSToCustomers smsToCustomers = new SMSToCustomers(trans.get().getDbpTransactionReference(), LocalDateTime.now(), trans.get().getCreditAccountID(), smsToCustomer, "MPESA", smsResult.isSmsSent());
                smsToCustomersRepo.save(smsToCustomers);
            }
            catch (Exception ex)
            {
                LOG.error("Error handling mobile transaction sms");
                LOG.error(ex);
            }

            ibCallbackRepo.save(trans.get());
        }else{
            LOG.error("null object");
        }
    }
}
