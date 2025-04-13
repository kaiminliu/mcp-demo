package com.demo.service.impl;

import com.demo.common.RtnCode;
import com.demo.dao.ProjectRepository;
import com.demo.dao.ProjectScoreRepository;
import com.demo.dao.UserRepository;
import com.demo.dto.ProjectScoreDTO;
import com.demo.entity.ProjectPO;
import com.demo.entity.ProjectScorePO;
import com.demo.entity.UserPO;
import com.demo.exception.CustomException;
import com.demo.service.ProjectScoreService;
import com.demo.util.ExcelUtil;
import com.demo.vo.ProjectScoreVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectScoreServiceImpl implements ProjectScoreService {
    
    private final ProjectScoreRepository projectScoreRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    
    @Override
    @Transactional
    public ProjectScoreDTO createProjectScore(ProjectScoreVO projectScoreVO) {
        if (projectScoreVO.getEmployeeId() == null || projectScoreVO.getProjectName() == null || projectScoreVO.getScore() == null) {
            throw new CustomException(RtnCode.EMPLOYEE_ID_PROJECT_NAME_SCORE_REQUIRED);
        }
        
        UserPO user = userRepository.findByEmployeeId(projectScoreVO.getEmployeeId())
                .orElseThrow(() -> new CustomException(RtnCode.USER_NOT_FOUND));
        
        ProjectPO project = projectRepository.findByProjectName(projectScoreVO.getProjectName())
                .orElseThrow(() -> new CustomException(RtnCode.PROJECT_NOT_FOUND));
        
        if (projectScoreVO.getEmployeeName() != null && !projectScoreVO.getEmployeeName().equals(user.getEmployeeName())) {
            throw new CustomException(RtnCode.USER_INFO_MISMATCH);
        }
        
        ProjectScorePO projectScorePO = new ProjectScorePO();
        projectScorePO.setUser(user);
        projectScorePO.setProject(project);
        projectScorePO.setScore(projectScoreVO.getScore());
        
        projectScorePO = projectScoreRepository.save(projectScorePO);
        return convertToDTO(projectScorePO);
    }
    
    @Override
    public List<ProjectScoreDTO> getProjectScores(String employeeId, String employeeName, Long projectId, String projectName, Double score) {
        List<ProjectScorePO> projectScores;
        
        if (employeeId != null && projectId != null) {
            projectScores = projectScoreRepository.findByUserEmployeeIdAndProjectId(employeeId, projectId);
        } else if (employeeId != null) {
            projectScores = projectScoreRepository.findByUserEmployeeId(employeeId);
        } else if (projectId != null) {
            projectScores = projectScoreRepository.findByProjectId(projectId);
        } else {
            projectScores = projectScoreRepository.findAll();
        }
        
        return projectScores.stream()
                .filter(ps -> employeeName == null || ps.getUser().getEmployeeName().equals(employeeName))
                .filter(ps -> projectName == null || ps.getProject().getProjectName().equals(projectName))
                .filter(ps -> score == null || ps.getScore().equals(score))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectScoreDTO> getProjectScores(String employeeId, String employeeName, Long projectId, String projectName, String score) {
        List<ProjectScorePO> projectScores;

        if (employeeId != null && projectId != null) {
            projectScores = projectScoreRepository.findByUserEmployeeIdAndProjectId(employeeId, projectId);
        } else if (employeeId != null) {
            projectScores = projectScoreRepository.findByUserEmployeeId(employeeId);
        } else if (projectId != null) {
            projectScores = projectScoreRepository.findByProjectId(projectId);
        } else {
            projectScores = projectScoreRepository.findAll();
        }
        return projectScores.stream()
                .filter(ps -> employeeName == null || ps.getUser().getEmployeeName().equals(employeeName))
                .filter(ps -> projectName == null || ps.getProject().getProjectName().equals(projectName))
                .filter(ps -> score == null ||
                        (score.split(",").length > 1 ?
                                (Double.valueOf(score.split(",")[0]) <= ps.getScore() && Double.valueOf(score.split(",")[1]) >= ps.getScore())
                                :
                                ps.getScore().equals(score)
                        ))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteProjectScore(String employeeId, String projectName) {
        if (employeeId == null || projectName == null) {
            throw new CustomException(RtnCode.EMPLOYEE_ID_AND_PROJECT_NAME_REQUIRED);
        }
        
        ProjectScorePO projectScore = projectScoreRepository.findByUserEmployeeIdAndProjectProjectName(employeeId, projectName)
                .orElseThrow(() -> new CustomException(RtnCode.SCORE_RECORD_NOT_FOUND));
        
        projectScoreRepository.delete(projectScore);
    }
    
    @Override
    @Transactional
    public ProjectScoreDTO updateProjectScore(String employeeId, String projectName, Double score) {
        if (employeeId == null || projectName == null || score == null) {
            throw new CustomException(RtnCode.EMPLOYEE_ID_PROJECT_NAME_SCORE_REQUIRED);
        }
        
        ProjectScorePO projectScore = projectScoreRepository.findByUserEmployeeIdAndProjectProjectName(employeeId, projectName)
                .orElseThrow(() -> new CustomException(RtnCode.SCORE_RECORD_NOT_FOUND));
        
        projectScore.setScore(score);
        projectScore = projectScoreRepository.save(projectScore);
        return convertToDTO(projectScore);
    }
    
    @Override
    public void exportProjectScores(HttpServletResponse response, OutputStream outputStream, String employeeId, String employeeName, Long projectId, String projectName, Double score) {
        List<ProjectScoreDTO> projectScores = getProjectScores(employeeId, employeeName, projectId, projectName, score);
        
        try {
            ExcelUtil.exportExcel(response, outputStream, projectScores, "project_scores");
        } catch (IOException e) {
            throw new CustomException(RtnCode.EXCEL_EXPORT_ERROR);
        }
    }

    @Override
    public void exportProjectScores(HttpServletResponse response, OutputStream outputStream, String employeeId, String employeeName, Long projectId, String projectName, String score) {
        List<ProjectScoreDTO> projectScores = getProjectScores(employeeId, employeeName, projectId, projectName, score);

        try {
            ExcelUtil.exportExcel(response, outputStream, projectScores, "project_scores");
        } catch (IOException e) {
            throw new CustomException(RtnCode.EXCEL_EXPORT_ERROR);
        }
    }
    
    private ProjectScoreDTO convertToDTO(ProjectScorePO projectScorePO) {
        ProjectScoreDTO projectScoreDTO = new ProjectScoreDTO();
        projectScoreDTO.setId(projectScorePO.getId());
        projectScoreDTO.setEmployeeId(projectScorePO.getUser().getEmployeeId());
        projectScoreDTO.setEmployeeName(projectScorePO.getUser().getEmployeeName());
        projectScoreDTO.setProjectId(projectScorePO.getProject().getId());
        projectScoreDTO.setProjectName(projectScorePO.getProject().getProjectName());
        projectScoreDTO.setScore(projectScorePO.getScore());
        return projectScoreDTO;
    }
} 