package com.demo.service;

import com.demo.dto.UserDTO;
import com.demo.vo.UserVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {
    
    UserDTO createUser(UserVO userVO);
    
    List<UserDTO> getUsers(String employeeId);
    
    void deleteUser(String employeeId);
    
    UserDTO updateUserName(String employeeId, String employeeName);
    
    void exportUsers(HttpServletResponse response, String employeeId);
} 