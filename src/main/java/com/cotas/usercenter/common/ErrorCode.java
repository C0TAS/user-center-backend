package com.cotas.usercenter.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200,"ok",""),
    PARAMS_ERROR(4000,"请求的参数错误",""),
    PARAMS_BLANK(4001,"请求的参数为空",""),
    NOT_AUTH(4010,"没有访问权限",""),
    NOT_LOGIN(4011,"用户未登录",""),
    SYSTEM_ERROR(5000,"系统异常","");


    private final Integer code;

    private final String message;

    private final String description;

    ErrorCode(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
