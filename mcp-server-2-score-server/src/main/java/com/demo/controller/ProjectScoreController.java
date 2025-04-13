package com.demo.controller;

import com.demo.common.CommonResp;
import com.demo.dto.ProjectScoreDTO;
import com.demo.service.ProjectScoreService;
import com.demo.vo.ProjectScoreVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/project-score")
@RequiredArgsConstructor
public class ProjectScoreController {
    
    private final ProjectScoreService projectScoreService;
    
    @PostMapping("/create")
    public CommonResp<ProjectScoreDTO> createProjectScore(@RequestBody ProjectScoreVO projectScoreVO) {
        return CommonResp.success(projectScoreService.createProjectScore(projectScoreVO));
    }
    
    @GetMapping("/list")
    public CommonResp<List<ProjectScoreDTO>> getProjectScores(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String score) {
        return CommonResp.success(projectScoreService.getProjectScores(employeeId, employeeName, projectId, projectName, score));
    }
    
    @PostMapping("/delete")
    public CommonResp<Void> deleteProjectScore(@RequestParam String employeeId, @RequestParam String projectName) {
        projectScoreService.deleteProjectScore(employeeId, projectName);
        return CommonResp.success(null);
    }
    
    @PostMapping("/update")
    public CommonResp<ProjectScoreDTO> updateProjectScore(
            @RequestParam String employeeId,
            @RequestParam String projectName,
            @RequestParam Double score) {
        return CommonResp.success(projectScoreService.updateProjectScore(employeeId, projectName, score));
    }
    
    @GetMapping("/export")
    public void exportProjectScores(
            HttpServletResponse response,
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String score) {
        projectScoreService.exportProjectScores(response, null, employeeId, employeeName, projectId, projectName, score);
    }
} 