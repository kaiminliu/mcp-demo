package com.demo.feign;

import com.demo.common.CommonResp;
import com.demo.dto.ProjectDTO;
import com.demo.dto.ProjectScoreDTO;
import com.demo.dto.UserDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.demo.vo.ProjectScoreVO;
import com.demo.vo.ProjectVO;
import com.demo.vo.UserVO;

import java.util.List;


@FeignClient("score-system")
public interface ScoreSystemClient {


    /**
        project
     */
    @PostMapping("/project/create")
    CommonResp<ProjectDTO> createProject(@RequestBody ProjectVO projectVO);

    @GetMapping("/project/list")
    CommonResp<List<ProjectDTO>> getProjects(@RequestParam(required = false) String projectName);

    @PostMapping("/project/delete")
    CommonResp<Void> deleteProject(@RequestParam Long projectId);

    @PostMapping("/project/update")
    CommonResp<ProjectDTO> updateProjectName(@RequestParam Long projectId, @RequestParam String projectName);

    /**
     project-score
     */
    @PostMapping("/project-score/create")
    CommonResp<ProjectScoreDTO> createProjectScore(@RequestBody ProjectScoreVO projectScoreVO);

    @GetMapping("/project-score/list")
    CommonResp<List<ProjectScoreDTO>> getProjectScores(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String score);

    @PostMapping("/project-score/delete")
    CommonResp<Void> deleteProjectScore(@RequestParam String employeeId, @RequestParam String projectName);

    @PostMapping("/project-score/update")
    CommonResp<ProjectScoreDTO> updateProjectScore(
            @RequestParam String employeeId,
            @RequestParam String projectName,
            @RequestParam Double score);

    @GetMapping("/export")
    void exportProjectScores(
            HttpServletResponse response,
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String score);

    /**
     user
     */
    @PostMapping("/user/create")
    CommonResp<UserDTO> createUser(@RequestBody UserVO userVO);

    @GetMapping("/user/list")
    CommonResp<List<UserDTO>> getUsers(@RequestParam(required = false) String employeeId);

    @PostMapping("/user/delete")
    CommonResp<Void> deleteUser(@RequestParam String employeeId);

    @PostMapping("/user/update")
    CommonResp<UserDTO> updateUserName(@RequestParam String employeeId, @RequestParam String employeeName);
}
