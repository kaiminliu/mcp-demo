package com.demo.common;

import lombok.Data;

@Data
public class CommonResp<T> {
    private int code;
    private String message;
    private T data;

    public static <T> CommonResp<T> success(T data) {
        CommonResp<T> resp = new CommonResp<>();
        resp.setCode(RtnCode.SUCCESS.getCode());
        resp.setMessage(RtnCode.SUCCESS.getMessage());
        resp.setData(data);
        return resp;
    }

    public static <T> CommonResp<T> error(RtnCode rtnCode) {
        CommonResp<T> resp = new CommonResp<>();
        resp.setCode(rtnCode.getCode());
        resp.setMessage(rtnCode.getMessage());
        return resp;
    }

    public static <T> CommonResp<T> error(RtnCode rtnCode, String message) {
        CommonResp<T> resp = new CommonResp<>();
        resp.setCode(rtnCode.getCode());
        resp.setMessage(message);
        return resp;
    }
} 