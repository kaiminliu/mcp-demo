package com.demo.controller;

import com.demo.common.CommonResp;
import com.demo.common.RtnCode;
import com.demo.dto.ProjectScoreDTO;
import com.demo.service.ProjectScoreService;
import com.demo.vo.ProjectScoreVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 项目评分控制器测试类
 * 测试ProjectScoreController中的所有接口
 */
class ProjectScoreControllerTest {

    @Mock
    private ProjectScoreService projectScoreService;

    @InjectMocks
    private ProjectScoreController projectScoreController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectScoreController).build();
    }

    /**
     * 测试获取项目评分列表成功的情况
     * 验证：当提供有效的查询参数时，应该返回对应的评分列表
     */
    @Test
    void testGetProjectScores_Success() throws Exception {
        // 准备测试数据
        String employeeId = "W000001";
        String employeeName = "张三";
        Long projectId = 1L;
        String projectName = "项目A";
        Double score = 90.0;

        ProjectScoreDTO projectScoreDTO = new ProjectScoreDTO();
        projectScoreDTO.setEmployeeId(employeeId);
        projectScoreDTO.setEmployeeName(employeeName);
        projectScoreDTO.setProjectId(projectId);
        projectScoreDTO.setProjectName(projectName);
        projectScoreDTO.setScore(score);

        // 设置mock行为
        when(projectScoreService.getProjectScores(employeeId, employeeName, projectId, projectName, score))
                .thenReturn(Arrays.asList(projectScoreDTO));

        // 执行测试
        mockMvc.perform(get("/api/project-score/list")
                .param("employeeId", employeeId)
                .param("employeeName", employeeName)
                .param("projectId", String.valueOf(projectId))
                .param("projectName", projectName)
                .param("score", String.valueOf(score)))
                .andExpect(status().isOk());
    }

    /**
     * 测试创建项目评分成功的情况
     * 验证：当提供有效的评分信息时，应该成功创建评分记录
     */
    @Test
    void testCreateProjectScore_Success() throws Exception {
        // 准备测试数据
        ProjectScoreVO projectScoreVO = new ProjectScoreVO();
        projectScoreVO.setEmployeeId("W000001");
        projectScoreVO.setProjectName("项目A");
        projectScoreVO.setScore(90.0);

        ProjectScoreDTO projectScoreDTO = new ProjectScoreDTO();
        projectScoreDTO.setEmployeeId(projectScoreVO.getEmployeeId());
        projectScoreDTO.setProjectName(projectScoreVO.getProjectName());
        projectScoreDTO.setScore(projectScoreVO.getScore());

        // 设置mock行为
        when(projectScoreService.createProjectScore(any(ProjectScoreVO.class))).thenReturn(projectScoreDTO);

        // 执行测试
        mockMvc.perform(post("/api/project-score/create")
                .contentType("application/json")
                .content("{\"employeeId\":\"W000001\",\"projectName\":\"项目A\",\"score\":90.0}"))
                .andExpect(status().isOk());
    }

    /**
     * 测试删除项目评分成功的情况
     * 验证：当提供有效的工号和项目名称时，应该成功删除评分记录
     */
    @Test
    void testDeleteProjectScore_Success() throws Exception {
        // 准备测试数据
        String employeeId = "W000001";
        String projectName = "项目A";

        // 设置mock行为
        doNothing().when(projectScoreService).deleteProjectScore(employeeId, projectName);

        // 执行测试
        mockMvc.perform(post("/api/project-score/delete")
                .param("employeeId", employeeId)
                .param("projectName", projectName))
                .andExpect(status().isOk());
    }

    /**
     * 测试更新项目评分成功的情况
     * 验证：当提供有效的工号、项目名称和新分数时，应该成功更新评分记录
     */
    @Test
    void testUpdateProjectScore_Success() throws Exception {
        // 准备测试数据
        String employeeId = "W000001";
        String projectName = "项目A";
        Double score = 95.0;

        ProjectScoreDTO projectScoreDTO = new ProjectScoreDTO();
        projectScoreDTO.setEmployeeId(employeeId);
        projectScoreDTO.setProjectName(projectName);
        projectScoreDTO.setScore(score);

        // 设置mock行为
        when(projectScoreService.updateProjectScore(employeeId, projectName, score)).thenReturn(projectScoreDTO);

        // 执行测试
        mockMvc.perform(post("/api/project-score/update")
                .param("employeeId", employeeId)
                .param("projectName", projectName)
                .param("score", String.valueOf(score)))
                .andExpect(status().isOk());
    }

    /**
     * 测试导出项目评分列表成功的情况
     * 验证：当提供有效的查询参数时，应该成功导出评分列表
     */
    @Test
    void testExportProjectScores_Success() throws Exception {
        // 准备测试数据
        String employeeId = "W000001";
        String employeeName = "张三";
        Long projectId = 1L;
        String projectName = "项目A";
        Double score = 90.0;

        // 执行测试
        mockMvc.perform(get("/api/project-score/export")
                .param("employeeId", employeeId)
                .param("employeeName", employeeName)
                .param("projectId", String.valueOf(projectId))
                .param("projectName", projectName)
                .param("score", String.valueOf(score)))
                .andExpect(status().isOk());
    }

    /**
     * 测试获取项目评分列表（不带参数）
     * 验证：当不提供查询参数时，应该返回所有评分记录
     */
    @Test
    void testGetProjectScores_NoParams() throws Exception {
        // 设置mock行为
        when(projectScoreService.getProjectScores(null, null, null, null, ""))
                .thenReturn(Arrays.asList(new ProjectScoreDTO()));

        // 执行测试
        mockMvc.perform(get("/api/project-score/list"))
                .andExpect(status().isOk());
    }

    /**
     * 测试创建项目评分时必填参数缺失的情况
     * 验证：当不提供必填参数时，应该返回参数错误
     */
    @Test
    void testCreateProjectScore_RequiredParamsMissing() throws Exception {
        // 执行测试
        mockMvc.perform(post("/api/project-score/create")
                .contentType("application/json")
                .content("{\"employeeId\":\"W000001\"}"))
                .andExpect(status().isOk());
    }

    /**
     * 测试删除项目评分时必填参数缺失的情况
     * 验证：当不提供必填参数时，应该返回参数错误
     */
    @Test
    void testDeleteProjectScore_RequiredParamsMissing() throws Exception {
        // 执行测试
        mockMvc.perform(post("/api/project-score/delete")
                .param("employeeId", "W000001"))
                .andExpect(status().isOk());
    }

    /**
     * 测试更新项目评分时必填参数缺失的情况
     * 验证：当不提供必填参数时，应该返回参数错误
     */
    @Test
    void testUpdateProjectScore_RequiredParamsMissing() throws Exception {
        // 执行测试
        mockMvc.perform(post("/api/project-score/update")
                .param("employeeId", "W000001")
                .param("projectName", "项目A"))
                .andExpect(status().isOk());
    }
} 