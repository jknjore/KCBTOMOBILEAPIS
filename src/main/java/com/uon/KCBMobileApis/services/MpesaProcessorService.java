package com.uon.KCBMobileApis.services;


import com.uon.KCBMobileApis.entities.BankToMobileTransferRequest;
import com.uon.KCBMobileApis.models.MobileRequest;
import com.uon.KCBMobileApis.repositories.IbCallbackRepo;
import com.uon.KCBMobileApis.responses.MobileOperatorsResponse;
import com.uon.KCBMobileApis.services.interfaces.ExternalProcessorsService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class MpesaProcessorService {
    private static final Logger LOG = LogManager.getLogger(MpesaProcessorService.class);
    @Autowired
    IbCallbackRepo ibTransferRepo;

    @Autowired
    ExternalProcessorsService externalProcessorsService;


    public MobileOperatorsResponse processMpesa(MobileRequest mobileRequest) {
        MobileOperatorsResponse mobileOperatorsResponse = new MobileOperatorsResponse();

        Optional<BankToMobileTransferRequest> fundsTransferRes = ibTransferRepo.findByDbpTransactionReference(mobileRequest.getDbpTransactionReference());
        if(fundsTransferRes.isPresent()){
            mobileOperatorsResponse.setMessage("Reference number already utilized");
        }else {
            BankToMobileTransferRequest bankToMobileTransferRequest = saveMobileRequest(mobileRequest,"Pending");
            if(externalProcessorsService.calltoMpesaAPIs(mobileRequest).getStatus() == "SUCCESS")
            {
                mobileOperatorsResponse.setStatus("SUCCESS");
                mobileOperatorsResponse.setMessage("Received for processing");
                mobileOperatorsResponse.setDbpTransactionNumber(bankToMobileTransferRequest.getDbpTransactionReference());
            }
            else {
                mobileOperatorsResponse.setStatus("FAILED");
                mobileOperatorsResponse.setMessage("Failed processing");
                mobileOperatorsResponse.setDbpTransactionNumber(bankToMobileTransferRequest.getDbpTransactionReference());
            }

            //process txn
        }
        return mobileOperatorsResponse;
    }



    public BankToMobileTransferRequest saveMobileRequest(@Valid MobileRequest mobileRequest, String status){
        BankToMobileTransferRequest ibTransferRequest = new BankToMobileTransferRequest();
        ibTransferRequest.setChargesAmount(BigDecimal.ZERO);
        ibTransferRequest.setTransactionCode("");
        ibTransferRequest.setDateCreated(LocalDateTime.now());
        //request
        ibTransferRequest.setDbpTransactionReference(mobileRequest.getDbpTransactionReference());
        ibTransferRequest.setDebitAccountID(mobileRequest.getDebitAccountID());
        ibTransferRequest.setCreditAccountID(mobileRequest.getCreditAccountMobile());
        ibTransferRequest.setAmount(mobileRequest.getAmount());
        ibTransferRequest.setDebitAmount(mobileRequest.getAmount());
        ibTransferRequest.setReceivingBankName(mobileRequest.getMobileMoneyOperatorName());

        ibTransferRequest.setStatus(status);

        ibTransferRepo.save(ibTransferRequest);

        return ibTransferRequest;
    }
}
