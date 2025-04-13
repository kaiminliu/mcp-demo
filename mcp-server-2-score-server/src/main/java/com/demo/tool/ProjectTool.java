package com.demo.tool;

import com.demo.common.CommonResp;
import com.demo.common.RtnCode;
import com.demo.dto.ProjectDTO;
import com.demo.service.ProjectService;
import com.demo.vo.ProjectVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ProjectTool {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletResponse response;

    @Tool(name = "createProject", description = "创建项目")
    public String createProject(@ToolParam(description = "项目信息", required = false) ProjectVO projectVO) {
        try {
            ProjectDTO projectDTO = projectService.createProject(projectVO);
            return objectMapper.writeValueAsString(CommonResp.success(projectDTO));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\",\"data\":null}";
            }
        }
    }

    @Tool(name = "getProjects", description = "查询项目列表")
    public String getProjects(@ToolParam(description = "项目名称", required = false) String projectName) {
        try {
            List<ProjectDTO> projectDTOs = projectService.getProjects(projectName);
            return objectMapper.writeValueAsString(CommonResp.success(projectDTOs));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\",\"data\":null}";
            }
        }
    }

    @Tool(name = "deleteProject", description = "删除项目")
    public String deleteProject(@ToolParam(description = "项目ID", required = false) Long projectId) {
        try {
            projectService.deleteProject(projectId);
            return objectMapper.writeValueAsString(CommonResp.success(null));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\",\"data\":null}";
            }
        }
    }

    @Tool(name = "updateProjectName", description = "更新项目名称")
    public String updateProjectName(
            @ToolParam(description = "项目ID", required = false) Long projectId,
            @ToolParam(description = "项目名称", required = false) String projectName) {
        try {
            ProjectDTO projectDTO = projectService.updateProjectName(projectId, projectName);
            return objectMapper.writeValueAsString(CommonResp.success(projectDTO));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\",\"data\":null}";
            }
        }
    }

//    @Tool(name = "exportProjects", description = "导出项目")
//    public String exportProjects(@ToolParam(description = "项目名称", required = false) String projectName) {
//        try {
//            projectService.exportProjects(response, projectName);
//            return objectMapper.writeValueAsString(CommonResp.success(null));
//        } catch (Exception e) {
//            try {
//                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.EXCEL_EXPORT_ERROR));
//            } catch (Exception ex) {
//                return "{\"code\":500,\"message\":\"系统错误\",\"data\":null}";
//            }
//        }
//    }
}