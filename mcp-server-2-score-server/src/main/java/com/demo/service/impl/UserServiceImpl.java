package com.demo.service.impl;

import com.demo.common.RtnCode;
import com.demo.dao.UserRepository;
import com.demo.dto.UserDTO;
import com.demo.entity.UserPO;
import com.demo.exception.CustomException;
import com.demo.service.UserService;
import com.demo.util.ExcelUtil;
import com.demo.vo.UserVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDTO createUser(UserVO userVO) {
        if (userVO.getEmployeeId() == null || userVO.getEmployeeName() == null) {
            throw new CustomException(RtnCode.EMPLOYEE_ID_AND_NAME_REQUIRED);
        }
        
        if (userRepository.existsByEmployeeId(userVO.getEmployeeId())) {
            throw new CustomException(RtnCode.USER_ALREADY_EXISTS);
        }
        
        UserPO userPO = new UserPO();
        userPO.setEmployeeId(userVO.getEmployeeId());
        userPO.setEmployeeName(userVO.getEmployeeName());
        
        userPO = userRepository.save(userPO);
        return convertToDTO(userPO);
    }
    
    @Override
    public List<UserDTO> getUsers(String employeeId) {
        if (employeeId != null) {
            return userRepository.findByEmployeeId(employeeId)
                    .map(user -> List.of(convertToDTO(user)))
                    .orElse(List.of());
        }
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteUser(String employeeId) {
        if (employeeId == null) {
            throw new CustomException(RtnCode.EMPLOYEE_ID_REQUIRED);
        }
        
        UserPO user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new CustomException(RtnCode.USER_NOT_FOUND));
        
        userRepository.delete(user);
    }
    
    @Override
    @Transactional
    public UserDTO updateUserName(String employeeId, String employeeName) {
        if (employeeId == null || employeeName == null) {
            throw new CustomException(RtnCode.EMPLOYEE_ID_AND_NAME_REQUIRED);
        }
        
        UserPO user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new CustomException(RtnCode.USER_NOT_FOUND));
        
        user.setEmployeeName(employeeName);
        user = userRepository.save(user);
        return convertToDTO(user);
    }
    
    @Override
    public void exportUsers(HttpServletResponse response, String employeeId) {
        List<UserDTO> users = getUsers(employeeId);
        
        try {
            ExcelUtil.exportExcel(response, null, users, "users");
        } catch (IOException e) {
            throw new CustomException(RtnCode.EXCEL_EXPORT_ERROR);
        }
    }
    
    private UserDTO convertToDTO(UserPO userPO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userPO.getId());
        userDTO.setEmployeeId(userPO.getEmployeeId());
        userDTO.setEmployeeName(userPO.getEmployeeName());
        userDTO.setCreatedTime(userPO.getCreatedTime());
        userDTO.setUpdatedTime(userPO.getUpdatedTime());
        return userDTO;
    }
} 