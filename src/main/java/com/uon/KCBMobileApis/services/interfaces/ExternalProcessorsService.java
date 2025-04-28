package com.uon.KCBMobileApis.services.interfaces;

import com.uon.KCBMobileApis.models.MobileRequest;
import com.uon.KCBMobileApis.responses.MobileOperatorsResponse;
import org.springframework.stereotype.Service;

@Service
public class ExternalProcessorsService implements ExternalProcessors{
    @Override
    public MobileOperatorsResponse calltoAirtelAPIs(MobileRequest mobileRequest) {
        return null;
    }

    @Override
    public MobileOperatorsResponse calltoMpesaAPIs(MobileRequest mobileRequest) {
        return null;
    }
}
