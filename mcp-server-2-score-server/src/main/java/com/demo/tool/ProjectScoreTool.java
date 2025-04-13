package com.demo.tool;

import com.demo.common.CommonResp;
import com.demo.common.RtnCode;
import com.demo.dto.ProjectScoreDTO;
import com.demo.service.ProjectScoreService;
import com.demo.vo.ProjectScoreVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.util.StringUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProjectScoreTool {

    @Autowired
    private ProjectScoreService projectScoreService;

    @Autowired
    private ObjectMapper objectMapper;


    @Tool(name = "createProjectScore", description = "创建项目评分")
    public String createProjectScore(@ToolParam(description = "项目评分信息", required = false) ProjectScoreVO projectScoreVO) {
        try {
            ProjectScoreDTO projectScoreDTO = projectScoreService.createProjectScore(projectScoreVO);
            return objectMapper.writeValueAsString(CommonResp.success(projectScoreDTO));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }

    @Tool(name = "getProjectScores", description = "查询项目评分列表")
    public String getProjectScores(
            @ToolParam(description = "工号", required = false) String employeeId,
            @ToolParam(description = "员工姓名", required = false) String employeeName,
            @ToolParam(description = "项目ID", required = false) Long projectId,
            @ToolParam(description = "项目名称", required = false) String projectName,
            @ToolParam(description = "分数（有多个分数，使用”,“连接；如果只有一个分数就是精确查询，如果有两个分数就是查询两分数区间内的数据）", required = false) String score
    ) {
        try {
            employeeId = StringUtil.isBlank(employeeId) ? null : employeeId;
            employeeName = StringUtil.isBlank(employeeName) ? null : employeeName;
            projectName = StringUtil.isBlank(projectName) ? null : projectName;
            List<ProjectScoreDTO> projectScoreDTOs = projectScoreService.getProjectScores(
                    employeeId, employeeName, projectId, projectName,
                    score);
            return objectMapper.writeValueAsString(CommonResp.success(projectScoreDTOs));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }

    @Tool(name = "deleteProjectScore", description = "删除项目评分")
    public String deleteProjectScore(
            @ToolParam(description = "工号", required = false) String employeeId,
            @ToolParam(description = "项目名称", required = false) String projectName) {
        try {
            projectScoreService.deleteProjectScore(employeeId, projectName);
            return objectMapper.writeValueAsString(CommonResp.success(null));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }

    @Tool(name = "updateProjectScore", description = "更新项目评分")
    public String updateProjectScore(
            @ToolParam(description = "工号", required = false) String employeeId,
            @ToolParam(description = "项目名称", required = false) String projectName,
            @ToolParam(description = "分数", required = false) Double score) {
        try {
            ProjectScoreDTO projectScoreDTO = projectScoreService.updateProjectScore(employeeId, projectName, score);
            return objectMapper.writeValueAsString(CommonResp.success(projectScoreDTO));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }

    @Tool(name = "exportProjectScores", description = "导出项目评分Excel文档")
    public String exportProjectScores(
            @ToolParam(description = "工号", required = false) String employeeId,
            @ToolParam(description = "员工姓名", required = false) String employeeName,
            @ToolParam(description = "项目ID", required = false) Long projectId,
            @ToolParam(description = "项目名称", required = false) String projectName,
            @ToolParam(description = "分数（有多个分数，使用”,“连接；如果只有一个分数就是精确查询，如果有两个分数就是查询两分数区间内的数据）", required = false) String score
    ) {
        try {
            StringBuilder linkBuilder = new StringBuilder("http://localhost:8082/api/project-score/export?n=1");

            Map<String, Object> params = new LinkedHashMap<>() {{
                put("employeeId", employeeId);
                put("employeeName", employeeName);
                put("projectId", projectId);
                put("projectName", projectName);
                put("score", score);
            }};

            params.forEach((key, value) -> {
                if (value != null && !(value instanceof String && ((String) value).isBlank())) {
                    linkBuilder.append("&").append(key).append("=").append(value);
                }
            });

            return "<a href='" + linkBuilder.toString() + "'>点击下载文件</a>";
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.EXCEL_EXPORT_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }
} 