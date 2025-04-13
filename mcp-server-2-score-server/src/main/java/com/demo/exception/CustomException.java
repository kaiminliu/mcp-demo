package com.demo.exception;

import com.demo.common.RtnCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final RtnCode rtnCode;

    public CustomException(RtnCode rtnCode) {
        super(rtnCode.getMessage());
        this.rtnCode = rtnCode;
    }

    public CustomException(RtnCode rtnCode, String message) {
        super(message);
        this.rtnCode = rtnCode;
    }
} 