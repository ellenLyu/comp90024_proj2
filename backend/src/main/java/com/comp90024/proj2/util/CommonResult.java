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
}
