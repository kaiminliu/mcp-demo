package com.demo.controller;

import com.demo.common.CommonResp;
import com.demo.common.RtnCode;
import com.demo.dto.UserDTO;
import com.demo.exception.CustomException;
import com.demo.service.UserService;
import com.demo.vo.UserVO;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 用户控制器测试类
 * 测试UserController中的所有接口
 */
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletResponse response;

    @Autowired
    private UserController userController;

    private MockMvc mockMvc;

    /**
     * 在每个测试方法执行前初始化测试环境
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    /**
     * 测试创建用户成功的情况
     * 验证：当提供有效的员工ID和姓名时，应该成功创建用户
     */
    @Test
    void testCreateUser_Success() throws Exception {
        // 准备测试数据
        UserVO userVO = new UserVO();
        userVO.setEmployeeId("W000001");
        userVO.setEmployeeName("张三");



        // 执行测试
        mockMvc.perform(post("/api/user/create")
                .contentType("application/json")
                .content("{\"employeeId\":\"W000001\",\"employeeName\":\"张三\"}"))
                .andExpect(status().isOk());
    }

    /**
     * 测试创建用户时用户已存在的情况
     * 验证：当尝试创建已存在的用户时，应该返回用户已存在的错误
     */
    @Test
    void testCreateUser_UserAlreadyExists() throws Exception {
        // 准备测试数据
        UserVO userVO = new UserVO();
        userVO.setEmployeeId("W000001");
        userVO.setEmployeeName("张三");

        // 设置mock行为
        when(userService.createUser(any(UserVO.class)))
                .thenThrow(new CustomException(RtnCode.USER_ALREADY_EXISTS));

        // 执行测试
        mockMvc.perform(post("/api/user/create")
                .contentType("application/json")
                .content("{\"employeeId\":\"W000001\",\"employeeName\":\"张三\"}"))
                .andExpect(status().isOk());
    }

    /**
     * 测试获取用户列表（带员工ID参数）
     * 验证：当提供员工ID时，应该返回对应的用户列表
     */
    @Test
    void testGetUsers_WithEmployeeId() throws Exception {
        // 准备测试数据
        String employeeId = "W000001";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeId(employeeId);
        userDTO.setEmployeeName("张三");

        // 设置mock行为
        when(userService.getUsers(employeeId)).thenReturn(Arrays.asList(userDTO));

        // 执行测试
        mockMvc.perform(get("/api/user/list")
                .param("employeeId", employeeId))
                .andExpect(status().isOk());
    }

    /**
     * 测试获取用户列表（不带参数）
     * 验证：当不提供员工ID时，应该返回所有用户列表
     */
    @Test
    void testGetUsers_NoParams() throws Exception {
        // 准备测试数据
        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeId("W000001");
        userDTO.setEmployeeName("张三");

        // 设置mock行为
        when(userService.getUsers(null)).thenReturn(Arrays.asList(userDTO));

        // 执行测试
        mockMvc.perform(get("/api/user/list"))
                .andExpect(status().isOk());
    }

    /**
     * 测试删除用户成功的情况
     * 验证：当提供有效的员工ID时，应该成功删除用户
     */
    @Test
    void testDeleteUser_Success() throws Exception {
        // 准备测试数据
        String employeeId = "W000001";

        // 设置mock行为
        doNothing().when(userService).deleteUser(employeeId);

        // 执行测试
        mockMvc.perform(post("/api/user/delete")
                .param("employeeId", employeeId))
                .andExpect(status().isOk());
    }

    /**
     * 测试删除不存在的用户
     * 验证：当尝试删除不存在的用户时，应该返回用户不存在的错误
     */
    @Test
    void testDeleteUser_UserNotFound() throws Exception {
        // 准备测试数据
        String employeeId = "W999999";

        // 设置mock行为
        doThrow(new CustomException(RtnCode.USER_NOT_FOUND))
                .when(userService).deleteUser(employeeId);

        // 执行测试
        mockMvc.perform(post("/api/user/delete")
                .param("employeeId", employeeId))
                .andExpect(status().isOk());
    }

    /**
     * 测试更新用户名成功的情况
     * 验证：当提供有效的员工ID和新用户名时，应该成功更新用户名
     */
    @Test
    void testUpdateUserName_Success() throws Exception {
        // 准备测试数据
        String employeeId = "W000001";
        String employeeName = "张三";

        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeId(employeeId);
        userDTO.setEmployeeName(employeeName);

        // 设置mock行为
        when(userService.updateUserName(employeeId, employeeName)).thenReturn(userDTO);

        // 执行测试
        mockMvc.perform(post("/api/user/update")
                .param("employeeId", employeeId)
                .param("employeeName", employeeName))
                .andExpect(status().isOk());
    }

    /**
     * 测试更新不存在的用户的用户名
     * 验证：当尝试更新不存在的用户的用户名时，应该返回用户不存在的错误
     */
    @Test
    void testUpdateUserName_UserNotFound() throws Exception {
        // 准备测试数据
        String employeeId = "W999999";
        String employeeName = "张三";

        // 设置mock行为
        when(userService.updateUserName(employeeId, employeeName))
                .thenThrow(new CustomException(RtnCode.USER_NOT_FOUND));

        // 执行测试
        mockMvc.perform(post("/api/user/update")
                .param("employeeId", employeeId)
                .param("employeeName", employeeName))
                .andExpect(status().isOk());
    }

    /**
     * 测试导出用户列表（带员工ID参数）
     * 验证：当提供员工ID时，应该成功导出对应的用户列表
     */
    @Test
    void testExportUsers_WithEmployeeId() throws Exception {
        // 准备测试数据
        String employeeId = "W000001";

        // 设置mock行为
        doNothing().when(userService).exportUsers(any(HttpServletResponse.class), eq(employeeId));

        // 执行测试
        mockMvc.perform(get("/api/user/export")
                .param("employeeId", employeeId))
                .andExpect(status().isOk());
    }

    /**
     * 测试导出用户列表（不带参数）
     * 验证：当不提供员工ID时，应该成功导出所有用户列表
     */
    @Test
    void testExportUsers_NoParams() throws Exception {
        // 设置mock行为
        doNothing().when(userService).exportUsers(any(HttpServletResponse.class), eq(null));

        // 执行测试
        mockMvc.perform(get("/api/user/export"))
                .andExpect(status().isOk());
    }

    /**
     * 测试导出用户列表时发生错误
     * 验证：当导出过程中发生错误时，应该返回导出错误
     */
    @Test
    void testExportUsers_ExportError() throws Exception {
        // 准备测试数据
        String employeeId = "W000001";

        // 设置mock行为
        doThrow(new CustomException(RtnCode.EXCEL_EXPORT_ERROR))
                .when(userService).exportUsers(any(HttpServletResponse.class), eq(employeeId));

        // 执行测试
        mockMvc.perform(get("/api/user/export")
                .param("employeeId", employeeId))
                .andExpect(status().isOk());
    }
} 