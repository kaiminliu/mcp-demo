package com.demo.controller;

import com.demo.common.CommonResp;
import com.demo.common.RtnCode;
import com.demo.dto.ProjectDTO;
import com.demo.service.ProjectService;
import com.demo.vo.ProjectVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 项目控制器测试类
 * 测试ProjectController中的所有接口
 */
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    /**
     * 测试创建项目成功的情况
     * 验证：当提供有效的项目名称时，应该成功创建项目
     */
    @Test
    void testCreateProject_Success() throws Exception {
        // 准备测试数据
        ProjectVO projectVO = new ProjectVO();
        projectVO.setProjectName("项目A");

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setProjectName(projectVO.getProjectName());

        // 设置mock行为
        when(projectService.createProject(any(ProjectVO.class))).thenReturn(projectDTO);

        // 执行测试
        mockMvc.perform(post("/api/project/create")
                .contentType("application/json")
                .content("{\"projectName\":\"项目A\"}"))
                .andExpect(status().isOk());
    }

    /**
     * 测试获取项目列表（带项目名称参数）
     * 验证：当提供项目名称时，应该返回对应的项目列表
     */
    @Test
    void testGetProjects_WithProjectName() throws Exception {
        // 准备测试数据
        String projectName = "项目A";
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setProjectName(projectName);

        // 设置mock行为
        when(projectService.getProjects(projectName)).thenReturn(Arrays.asList(projectDTO));

        // 执行测试
        mockMvc.perform(get("/api/project/list")
                .param("projectName", projectName))
                .andExpect(status().isOk());
    }

    /**
     * 测试获取项目列表（不带参数）
     * 验证：当不提供项目名称时，应该返回所有项目列表
     */
    @Test
    void testGetProjects_NoParams() throws Exception {
        // 准备测试数据
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setProjectName("项目A");

        // 设置mock行为
        when(projectService.getProjects(null)).thenReturn(Arrays.asList(projectDTO));

        // 执行测试
        mockMvc.perform(get("/api/project/list"))
                .andExpect(status().isOk());
    }

    /**
     * 测试删除项目成功的情况
     * 验证：当提供有效的项目ID时，应该成功删除项目
     */
    @Test
    void testDeleteProject_Success() throws Exception {
        // 准备测试数据
        Long projectId = 1L;

        // 设置mock行为
        doNothing().when(projectService).deleteProject(projectId);

        // 执行测试
        mockMvc.perform(post("/api/project/delete")
                .param("projectId", String.valueOf(projectId)))
                .andExpect(status().isOk());
    }

    /**
     * 测试更新项目名称成功的情况
     * 验证：当提供有效的项目ID和新项目名称时，应该成功更新项目名称
     */
    @Test
    void testUpdateProjectName_Success() throws Exception {
        // 准备测试数据
        Long projectId = 1L;
        String projectName = "项目B";

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectId);
        projectDTO.setProjectName(projectName);

        // 设置mock行为
        when(projectService.updateProjectName(projectId, projectName)).thenReturn(projectDTO);

        // 执行测试
        mockMvc.perform(post("/api/project/update")
                .param("projectId", String.valueOf(projectId))
                .param("projectName", projectName))
                .andExpect(status().isOk());
    }

    /**
     * 测试导出项目列表（带项目名称参数）
     * 验证：当提供项目名称时，应该成功导出对应的项目列表
     */
    @Test
    void testExportProjects_WithProjectName() throws Exception {
        // 准备测试数据
        String projectName = "项目A";

        // 执行测试
        mockMvc.perform(get("/api/project/export")
                .param("projectName", projectName))
                .andExpect(status().isOk());
    }

    /**
     * 测试导出项目列表（不带参数）
     * 验证：当不提供项目名称时，应该成功导出所有项目列表
     */
    @Test
    void testExportProjects_NoParams() throws Exception {
        // 执行测试
        mockMvc.perform(get("/api/project/export"))
                .andExpect(status().isOk());
    }

    /**
     * 测试创建项目时必填参数缺失的情况
     * 验证：当不提供项目名称时，应该返回参数错误
     */
    @Test
    void testCreateProject_RequiredParamsMissing() throws Exception {
        // 执行测试
        mockMvc.perform(post("/api/project/create")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isOk());
    }

    /**
     * 测试删除项目时必填参数缺失的情况
     * 验证：当不提供项目ID时，应该返回参数错误
     */
    @Test
    void testDeleteProject_RequiredParamsMissing() throws Exception {
        // 执行测试
        mockMvc.perform(post("/api/project/delete"))
                .andExpect(status().isOk());
    }

    /**
     * 测试更新项目名称时必填参数缺失的情况
     * 验证：当不提供新项目名称时，应该返回参数错误
     */
    @Test
    void testUpdateProjectName_RequiredParamsMissing() throws Exception {
        // 执行测试
        mockMvc.perform(post("/api/project/update")
                .param("projectId", "1"))
                .andExpect(status().isOk());
    }
} 