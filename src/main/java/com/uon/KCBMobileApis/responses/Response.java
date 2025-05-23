package com.uon.KCBMobileApis.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private boolean isSuccess;
    private String message;
    private Object data;
}
