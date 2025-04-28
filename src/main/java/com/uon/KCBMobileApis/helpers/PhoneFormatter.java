package com.uon.KCBMobileApis.helpers;

import jakarta.validation.constraints.NotNull;

public class PhoneFormatter {
    public String numberCheck(@NotNull String clientNumber){
        if(clientNumber.startsWith("+") && clientNumber.length() == 13){
            return clientNumber.substring(1);
        }else if(clientNumber.startsWith("254") && clientNumber.length() == 12){
            return clientNumber;
        }else if(clientNumber.length() == 9){
            return "254"+clientNumber;
        }else if(clientNumber.startsWith("07") && clientNumber.length() == 10){
            return "254"+clientNumber.substring(1);
        }else{
            return clientNumber;//254709485000
        }
    }
}
