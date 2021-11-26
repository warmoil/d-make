package com.example.dmake.exception;

import lombok.Getter;

@Getter
public class DeveloperMakerException extends RuntimeException{
    private DeveloperErrorCode developerErrorCode;
    private String detailMessage;

    public DeveloperMakerException(DeveloperErrorCode errorCode) {
        super(errorCode.getMessage());
        this.developerErrorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }

    public DeveloperMakerException(DeveloperErrorCode errorCode ,String detailMessage) {
        super(errorCode.getMessage());
        this.developerErrorCode = errorCode;
        this.detailMessage = detailMessage;
    }

}
