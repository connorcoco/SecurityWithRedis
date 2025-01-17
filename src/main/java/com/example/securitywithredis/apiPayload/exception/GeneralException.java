package com.example.securitywithredis.apiPayload.exception;

import com.example.securitywithredis.apiPayload.code.BaseCode;
import com.example.securitywithredis.apiPayload.code.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseCode code;

    public ResponseDTO getErrorReason() {
        return this.code.getReason();
    }

    public ResponseDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}