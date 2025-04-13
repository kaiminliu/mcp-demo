package com.demo.servcie;

import com.demo.common.CommonResp;
import com.demo.common.RtnCode;
import com.demo.feign.ScoreSystemClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.dto.ProjectDTO;
import com.demo.dto.ProjectScoreDTO;
import com.demo.dto.UserDTO;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.demo.vo.ProjectScoreVO;
import com.demo.vo.ProjectVO;
import com.demo.vo.UserVO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ScoreSystemService {

    private final ScoreSystemClient scoreSystemClient;

    private final ObjectMapper objectMapper;

    @Value("${spring.cloud.openfeign.client.config.score-system.url}")
    private String scoreSystemUrl;

    /**
        project
    */
    @Tool(name = "createProject", description = "创建项目")
    public String createProject(@ToolParam(description = "项目信息", required = false)  ProjectVO projectVO) {
        try {
            CommonResp<ProjectDTO> resp = scoreSystemClient.createProject(projectVO);
            return objectMapper.writeValueAsString(resp);
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
            CommonResp<List<ProjectDTO>> resp = scoreSystemClient.getProjects(projectName);
            return objectMapper.writeValueAsString(resp);
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
            scoreSystemClient.deleteProject(projectId);
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
            CommonResp<ProjectDTO> resp = scoreSystemClient.updateProjectName(projectId, projectName);
            return objectMapper.writeValueAsString(resp);
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\",\"data\":null}";
            }
        }
    }


    /**
     project-score
     */
    @Tool(name = "createProjectScore", description = "创建项目评分")
    public String createProjectScore(@ToolParam(description = "项目评分信息", required = false) ProjectScoreVO projectScoreVO) {
        try {
            CommonResp<ProjectScoreDTO> resp = scoreSystemClient.createProjectScore(projectScoreVO);
            return objectMapper.writeValueAsString(resp);
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
            employeeId = StringUtils.isBlank(employeeId) ? null : employeeId;
            employeeName = StringUtils.isBlank(employeeName) ? null : employeeName;
            projectName = StringUtils.isBlank(projectName) ? null : projectName;
            CommonResp<List<ProjectScoreDTO>> resp = scoreSystemClient.getProjectScores(
                    employeeId, employeeName, projectId, projectName,
                    score);
            return objectMapper.writeValueAsString(resp);
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
            scoreSystemClient.deleteProjectScore(employeeId, projectName);
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
            CommonResp<ProjectScoreDTO> resp = scoreSystemClient.updateProjectScore(employeeId, projectName, score);
            return objectMapper.writeValueAsString(resp);
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
            StringBuilder linkBuilder = new StringBuilder(scoreSystemUrl+"/project-score/export?n=1");

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

    /**
     user
     */
    @Tool(name = "createUser", description = "创建用户")
    public String createUser(@ToolParam(description = "用户信息", required = false)  UserVO userVO) {
        try {
            CommonResp<UserDTO> resp = scoreSystemClient.createUser(userVO);
            return objectMapper.writeValueAsString(resp);
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }

    @Tool(name = "getUsers", description = "查询用户列表")
    public String getUsers(@ToolParam(description = "工号", required = false) String employeeId) {
        try {
            CommonResp<List<UserDTO>> resp = scoreSystemClient.getUsers(employeeId);
            return objectMapper.writeValueAsString(resp);
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }

    @Tool(name = "deleteUser", description = "删除用户")
    public String deleteUser(@ToolParam(description = "工号", required = false) String employeeId) {
        try {
            scoreSystemClient.deleteUser(employeeId);
            return objectMapper.writeValueAsString(CommonResp.success(null));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }

    @Tool(name = "updateUserName", description = "更新用户姓名")
    public String updateUserName(
            @ToolParam(description = "工号", required = false) String employeeId,
            @ToolParam(description = "姓名", required = false) String employeeName) {
        try {
            CommonResp<UserDTO> resp = scoreSystemClient.updateUserName(employeeId, employeeName);
            return objectMapper.writeValueAsString(resp);
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }
}
