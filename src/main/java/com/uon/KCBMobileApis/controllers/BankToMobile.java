package com.uon.KCBMobileApis.controllers;

import com.uon.KCBMobileApis.models.MobileRequest;
import com.uon.KCBMobileApis.models.MpesaCallBack;
import com.uon.KCBMobileApis.responses.MobileOperatorsResponse;
import com.uon.KCBMobileApis.services.MobileProcessorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class BankToMobile {
    private static final Logger LOG = LogManager.getLogger(BankToMobile.class);
    @Autowired
    MobileProcessorService mobileProcessor;

    @PostMapping("/mobile")
    public ResponseEntity<?> processMobile(
            @Valid @RequestBody MobileRequest mobileRequest,
            HttpServletRequest request
    ) {
        // log
        LOG.info("-------------------- MOBILE TRANSFER REQUEST START --------------------");
        LOG.info("Bank to Mobile request at "+ LocalDateTime.now());
        LOG.info("Incoming Request Source IP ADDRESS :: {}", request.getRemoteAddr());
        LOG.info("Incoming Request Source HOSTNAME :: {}", request.getServerName());
        LOG.info("Incoming Request Source IP ADDRESS X-FORWARDED-FOR :: {}", request.getHeader("X-FORWARDED-FOR"));
        LOG.info("-------------------- MOBILE TRANSFER REQUEST END --------------------");
        MobileOperatorsResponse response = mobileProcessor.mobileProcess(mobileRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/mpesa/callback")
    public void callback(@RequestBody(required = true) MpesaCallBack callback) {
        LOG.info("Mpesa callback at "+ LocalDateTime.now());
        LOG.info(callback);
        mobileProcessor.callback(callback);
    }
}
