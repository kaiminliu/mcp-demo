package com.demo.service.impl;

import com.demo.common.RtnCode;
import com.demo.dao.ProjectRepository;
import com.demo.dao.ProjectScoreRepository;
import com.demo.dto.ProjectDTO;
import com.demo.entity.ProjectPO;
import com.demo.exception.CustomException;
import com.demo.service.ProjectService;
import com.demo.util.ExcelUtil;
import com.demo.vo.ProjectVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProjectScoreRepository projectScoreRepository;
    
    @Override
    @Transactional
    public ProjectDTO createProject(ProjectVO projectVO) {
        if (projectVO.getProjectName() == null) {
            throw new CustomException(RtnCode.PROJECT_NAME_REQUIRED);
        }
        
        if (projectRepository.existsByProjectName(projectVO.getProjectName())) {
            throw new CustomException(RtnCode.PROJECT_ALREADY_EXISTS);
        }
        
        ProjectPO projectPO = new ProjectPO();
        projectPO.setProjectName(projectVO.getProjectName());
        
        projectPO = projectRepository.save(projectPO);
        return convertToDTO(projectPO);
    }
    
    @Override
    public List<ProjectDTO> getProjects(String projectName) {
        if (projectName != null) {
            return projectRepository.findByProjectNameContaining(projectName).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        return projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        if (projectId == null) {
            throw new CustomException(RtnCode.PARAM_ERROR);
        }
        
        if (projectScoreRepository.existsByProjectId(projectId)) {
            throw new CustomException(RtnCode.PROJECT_HAS_RELATED_DATA);
        }
        
        ProjectPO project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(RtnCode.PROJECT_NOT_FOUND));
        
        projectRepository.delete(project);
    }
    
    @Override
    @Transactional
    public ProjectDTO updateProjectName(Long projectId, String projectName) {
        if (projectId == null || projectName == null) {
            throw new CustomException(RtnCode.PROJECT_ID_AND_NAME_REQUIRED);
        }
        
        ProjectPO project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(RtnCode.PROJECT_NOT_FOUND));
        
        if (projectRepository.existsByProjectName(projectName)) {
            throw new CustomException(RtnCode.PROJECT_ALREADY_EXISTS);
        }
        
        project.setProjectName(projectName);
        project = projectRepository.save(project);
        return convertToDTO(project);
    }
    
    @Override
    public void exportProjects(HttpServletResponse response, String projectName) {
        List<ProjectDTO> projects = getProjects(projectName);
        
        try {

            ExcelUtil.exportExcel(response, null, projects, "projects");
        } catch (IOException e) {
            throw new CustomException(RtnCode.EXCEL_EXPORT_ERROR);
        }
    }
    
    private ProjectDTO convertToDTO(ProjectPO projectPO) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectPO.getId());
        projectDTO.setProjectName(projectPO.getProjectName());
        return projectDTO;
    }
} 