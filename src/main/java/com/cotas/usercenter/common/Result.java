package com.cotas.usercenter.common;

public class Result {

    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(ErrorCode.SUCCESS,data);
    }

    /**
     * 失败
     * @param errorCode
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode,T data){
        return new BaseResponse<>(errorCode,data);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败，直接传code，给自定义异常使用
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(Integer code, String message, String description) {
        return new BaseResponse<>(code,message,description);
    }

    /**
     * 失败，传errorCode，给自定义异常使用
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode,message,description);
    }
}
