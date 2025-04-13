package com.demo.service;

import com.demo.dto.ProjectDTO;
import com.demo.vo.ProjectVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ProjectService {
    
    ProjectDTO createProject(ProjectVO projectVO);
    
    List<ProjectDTO> getProjects(String projectName);
    
    void deleteProject(Long projectId);
    
    ProjectDTO updateProjectName(Long projectId, String projectName);
    
    void exportProjects(HttpServletResponse response, String projectName);
} 