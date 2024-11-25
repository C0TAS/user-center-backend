package com.cotas.usercenter.exception;

import com.cotas.usercenter.common.ErrorCode;
import lombok.Getter;

/**
 * 拓展异常信息
 */
@Getter
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 1056758525320708330L;

    private final Integer code;

    private final String description;

    public BusinessException(String message, Integer code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    /**
     * 直接传入设置好的错误码
     * @param errorCode
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    /**
     * 直接传入设置好的错误码,自己写详情
     * @param errorCode
     * @param description
     */
     public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }
}
