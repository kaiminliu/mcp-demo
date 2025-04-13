package com.demo.exception;

import com.demo.common.CommonResp;
import com.demo.common.RtnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public CommonResp<Void> handleCustomException(CustomException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        return CommonResp.error(e.getRtnCode());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public CommonResp<Void> handleValidException(Exception e) {
        log.error("参数校验异常: {}", e.getMessage(), e);
        String message = "参数校验失败";
        if (e instanceof MethodArgumentNotValidException) {
            FieldError fieldError = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError();
            if (fieldError != null) {
                message = fieldError.getDefaultMessage();
            }
        } else if (e instanceof BindException) {
            FieldError fieldError = ((BindException) e).getBindingResult().getFieldError();
            if (fieldError != null) {
                message = fieldError.getDefaultMessage();
            }
        }
        return CommonResp.error(RtnCode.PARAM_ERROR, message);
    }

    @ExceptionHandler(Exception.class)
    public CommonResp<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return CommonResp.error(RtnCode.SYSTEM_ERROR);
    }
} 