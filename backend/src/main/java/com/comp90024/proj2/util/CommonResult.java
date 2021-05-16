package com.comp90024.proj2.util;

public class CommonResult<T> {

    private String status;

    private String message;

    private T data;

    public CommonResult(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(Consts.ResultCode.SUCCESS, message, data);
    }

    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>("500", message, null);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
