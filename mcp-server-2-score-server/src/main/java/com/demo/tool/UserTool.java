package com.demo.tool;

import com.demo.common.CommonResp;
import com.demo.common.RtnCode;
import com.demo.dto.UserDTO;
import com.demo.exception.CustomException;
import com.demo.service.UserService;
import com.demo.vo.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserTool {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletResponse response;

    @Tool(name = "createUser", description = "创建用户")
    public String createUser(@ToolParam(description = "用户信息", required = false) UserVO userVO) {
        try {
            UserDTO userDTO = userService.createUser(userVO);
            return objectMapper.writeValueAsString(CommonResp.success(userDTO));
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
            List<UserDTO> userDTOs = userService.getUsers(employeeId);
            return objectMapper.writeValueAsString(CommonResp.success(userDTOs));
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
            userService.deleteUser(employeeId);
            return objectMapper.writeValueAsString(CommonResp.success(null));
        } catch (CustomException e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(e.getRtnCode()));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
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
            UserDTO userDTO = userService.updateUserName(employeeId, employeeName);
            return objectMapper.writeValueAsString(CommonResp.success(userDTO));
        } catch (Exception e) {
            try {
                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.SYSTEM_ERROR));
            } catch (Exception ex) {
                return "{\"code\":500,\"message\":\"系统错误\"}";
            }
        }
    }

//    @Tool(name = "exportUsers", description = "导出用户")
//    public String exportUsers(@ToolParam(description = "工号", required = false) String employeeId) {
//        try {
//            userService.exportUsers(response, employeeId);
//            return objectMapper.writeValueAsString(CommonResp.success(null));
//        } catch (Exception e) {
//            try {
//                return objectMapper.writeValueAsString(CommonResp.error(RtnCode.EXCEL_EXPORT_ERROR));
//            } catch (Exception ex) {
//                return "{\"code\":500,\"message\":\"系统错误\"}";
//            }
//        }
//    }
}