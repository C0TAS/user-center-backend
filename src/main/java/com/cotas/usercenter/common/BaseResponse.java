package com.cotas.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义返回信息
 * @param <T>
 */
@Data
public class BaseResponse<T>
{

    private static final long serialVersionUID = -5210292055313018198L;

    private Integer code;

    private T data;

    private String message;

    private String description;


    public BaseResponse(ErrorCode errorCode,T data) {
        this.code = errorCode.getCode();
        this.data = data;
        this.message = errorCode.getMessage();
        this.description = errorCode.getDescription();
    }

    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.description = errorCode.getDescription();
    }


    public BaseResponse(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(ErrorCode errorCode, String message, String description) {
        this.code = errorCode.getCode();
        this.message = message;
        this.description = description;
    }
}
