package com.demo.common;

import lombok.Getter;

@Getter
public enum RtnCode {
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    PARAM_ERROR(5001, "参数错误"),
    USER_NOT_FOUND(5002, "用户不存在"),
    PROJECT_NOT_FOUND(5003, "项目不存在"),
    USER_ALREADY_EXISTS(5004, "用户已存在"),
    PROJECT_ALREADY_EXISTS(5005, "项目已存在"),
    PROJECT_NAME_ALREADY_EXISTS(5021, "项目名称已存在"),
    SCORE_RECORD_NOT_FOUND(5006, "评分记录不存在"),
    CANNOT_DELETE_USER(5007, "存在关联数据，用户无法删除"),
    CANNOT_DELETE_PROJECT(5008, "存在关联数据，项目无法删除"),
    EXCEL_EXPORT_ERROR(5009, "Excel导出失败"),
    SYSTEM_ERROR(5010, "系统错误"),
    USER_INFO_MISMATCH(5011, "用户信息不匹配"),
    PROJECT_HAS_RELATED_DATA(5012, "专案存在关联数据"),
    EMPLOYEE_ID_REQUIRED(5013, "工号不能为空"),
    EMPLOYEE_NAME_REQUIRED(5014, "姓名不能为空"),
    PROJECT_NAME_REQUIRED(5015, "专案名称不能为空"),
    SCORE_REQUIRED(5016, "分数不能为空"),
    EMPLOYEE_ID_AND_NAME_REQUIRED(5017, "工号和姓名不能为空"),
    EMPLOYEE_ID_AND_PROJECT_NAME_REQUIRED(5018, "工号和专案名称不能为空"),
    PROJECT_ID_AND_NAME_REQUIRED(5019, "专案ID和名称不能为空"),
    EMPLOYEE_ID_PROJECT_NAME_SCORE_REQUIRED(5020, "工号、专案名称和分数不能为空");

    private final int code;
    private final String message;

    RtnCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
} 