package com.example.dmake.exception;

import com.example.dmake.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

//각 컨트롤러에다가 참견하는 어노테이션
@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {
    @ExceptionHandler(DeveloperMakerException.class)
    public DMakerErrorResponse handleException(DeveloperMakerException e) {
        log.error("errorCode : {} , errorMessage : {}",e.getDeveloperErrorCode(),e.getMessage());
        return DMakerErrorResponse.builder()
                .errorCode(e.getDeveloperErrorCode())
                .errorMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class, MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handleBadRequest(Exception e , HttpServletRequest request) {
        log.error("url : {} , message : {}",request.getRequestURI() , e.getMessage());
        return  DMakerErrorResponse.builder()
                .errorCode(DeveloperErrorCode.INVALID_REQUEST)
                .errorMessage(DeveloperErrorCode.INVALID_REQUEST.getMessage())
                .build();
    }

}
