package com.demo.controller;

import com.demo.common.CommonResp;
import com.demo.dto.ProjectDTO;
import com.demo.service.ProjectService;
import com.demo.vo.ProjectVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;
    
    @PostMapping("/create")
    public CommonResp<ProjectDTO> createProject(@RequestBody ProjectVO projectVO) {
        return CommonResp.success(projectService.createProject(projectVO));
    }
    
    @GetMapping("/list")
    public CommonResp<List<ProjectDTO>> getProjects(@RequestParam(required = false) String projectName) {
        return CommonResp.success(projectService.getProjects(projectName));
    }
    
    @PostMapping("/delete")
    public CommonResp<Void> deleteProject(@RequestParam Long projectId) {
        projectService.deleteProject(projectId);
        return CommonResp.success(null);
    }
    
    @PostMapping("/update")
    public CommonResp<ProjectDTO> updateProjectName(@RequestParam Long projectId, @RequestParam String projectName) {
        return CommonResp.success(projectService.updateProjectName(projectId, projectName));
    }
    
    @GetMapping("/export")
    public void exportProjects(HttpServletResponse response, @RequestParam(required = false) String projectName) {
        projectService.exportProjects(response, projectName);
    }
} 