package com.demo.service;

import com.demo.dto.ProjectScoreDTO;
import com.demo.vo.ProjectScoreVO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.util.List;

public interface ProjectScoreService {
    
    ProjectScoreDTO createProjectScore(ProjectScoreVO projectScoreVO);
    
    List<ProjectScoreDTO> getProjectScores(String employeeId, String employeeName, Long projectId, String projectName, Double score);

    List<ProjectScoreDTO> getProjectScores(String employeeId, String employeeName, Long projectId, String projectName, String score);
    
    void deleteProjectScore(String employeeId, String projectName);
    
    ProjectScoreDTO updateProjectScore(String employeeId, String projectName, Double score);
    
    void exportProjectScores(HttpServletResponse response, OutputStream outputStream, String employeeId, String employeeName, Long projectId, String projectName, Double score);

    void exportProjectScores(HttpServletResponse response, OutputStream outputStream, String employeeId, String employeeName, Long projectId, String projectName, String score);
}