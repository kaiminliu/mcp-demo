package com.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户数据传输对象
 */
@Data
public class UserDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 员工工号
     */
    private String employeeId;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
} 