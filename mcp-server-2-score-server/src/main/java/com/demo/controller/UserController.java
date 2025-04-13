package com.demo.controller;

import com.demo.common.CommonResp;
import com.demo.dto.UserDTO;
import com.demo.service.UserService;
import com.demo.vo.UserVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/create")
    public CommonResp<UserDTO> createUser(@RequestBody UserVO userVO) {
        return CommonResp.success(userService.createUser(userVO));
    }
    
    @GetMapping("/list")
    public CommonResp<List<UserDTO>> getUsers(@RequestParam(required = false) String employeeId) {
        return CommonResp.success(userService.getUsers(employeeId));
    }
    
    @PostMapping("/delete")
    public CommonResp<Void> deleteUser(@RequestParam String employeeId) {
        userService.deleteUser(employeeId);
        return CommonResp.success(null);
    }
    
    @PostMapping("/update")
    public CommonResp<UserDTO> updateUserName(@RequestParam String employeeId, @RequestParam String employeeName) {
        return CommonResp.success(userService.updateUserName(employeeId, employeeName));
    }
    
    @GetMapping("/export")
    public void exportUsers(HttpServletResponse response, @RequestParam(required = false) String employeeId) {
        userService.exportUsers(response, employeeId);
    }
} 