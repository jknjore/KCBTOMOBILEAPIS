package com.uon.KCBMobileApis.services;


import com.uon.KCBMobileApis.entities.BankToMobileTransferRequest;
import com.uon.KCBMobileApis.models.MobileRequest;
import com.uon.KCBMobileApis.repositories.IbCallbackRepo;
import com.uon.KCBMobileApis.responses.MobileOperatorsResponse;
import com.uon.KCBMobileApis.services.interfaces.ExternalProcessorsService;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AirtelProcessorService {
    private static final Logger LOG = LogManager.getLogger(AirtelProcessorService.class);
    @Autowired
    IbCallbackRepo ibTransferRepo;
    @Autowired
    ExternalProcessorsService externalProcessorsService;

    @SneakyThrows
    public MobileOperatorsResponse processAirtel(MobileRequest mobileRequest) {
        MobileOperatorsResponse mobileOperatorsResponse = new MobileOperatorsResponse();

        Optional<BankToMobileTransferRequest> fundsTransferRes = ibTransferRepo.findByDbpTransactionReference(mobileRequest.getDbpTransactionReference());
        if(fundsTransferRes.isPresent()){
            mobileOperatorsResponse.setMessage("Reference number already utilized");
        }else {
            BankToMobileTransferRequest bankToMobileTransferRequest = new MpesaProcessorService().saveMobileRequest(mobileRequest,"Pending");
            MobileOperatorsResponse response = externalProcessorsService.calltoAirtelAPIs(mobileRequest);
            if(response.getStatus().equals("SUCCESS"))
            {
                mobileOperatorsResponse.setStatus("SUCCESS");
                mobileOperatorsResponse.setMessage("Received for processing");
                mobileOperatorsResponse.setUniqueTransactionReference(response.getUniqueTransactionReference());
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
}
