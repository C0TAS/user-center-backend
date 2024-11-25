package com.cotas.usercenter.exception;

import com.cotas.usercenter.common.BaseResponse;
import com.cotas.usercenter.common.ErrorCode;
import com.cotas.usercenter.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse BusinessExceptionHandler(BusinessException e){
        log.error("BusinessException = " + e.getMessage());
        return Result.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse RunTimeExceptionHandler(RuntimeException e){
        log.error("RunTimeException = " + e.getMessage());
        return Result.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
