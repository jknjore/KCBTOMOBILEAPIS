package com.uon.KCBMobileApis.services.interfaces;

import com.uon.KCBMobileApis.models.MobileRequest;
import com.uon.KCBMobileApis.models.MpesaCallBack;
import com.uon.KCBMobileApis.responses.MobileOperatorsResponse;
import jakarta.validation.Valid;

public interface MobileProcessor {
    public MobileOperatorsResponse mobileProcess(@Valid MobileRequest mobileRequest);
    public void callback(MpesaCallBack callback);
}
