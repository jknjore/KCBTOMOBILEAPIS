package com.uon.KCBMobileApis.services.interfaces;

import com.uon.KCBMobileApis.models.MobileRequest;
import com.uon.KCBMobileApis.responses.MobileOperatorsResponse;
import jakarta.validation.Valid;

public interface ExternalProcessors {
    public MobileOperatorsResponse calltoAirtelAPIs(@Valid MobileRequest mobileRequest);
    public MobileOperatorsResponse calltoMpesaAPIs(@Valid MobileRequest mobileRequest);
}
