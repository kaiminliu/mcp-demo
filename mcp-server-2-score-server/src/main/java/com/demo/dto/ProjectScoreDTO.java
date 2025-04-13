package com.demo.dto;

import lombok.Data;

@Data
public class ProjectScoreDTO {
    private Long id;
    private String employeeId;
    private String employeeName;
    private Long projectId;
    private String projectName;
    private Double score;
} 