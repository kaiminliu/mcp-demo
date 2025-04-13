package com.demo.dao;

import com.demo.entity.ProjectScorePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectScoreRepository extends JpaRepository<ProjectScorePO, Long> {
    
    List<ProjectScorePO> findByUserEmployeeIdAndProjectId(String employeeId, Long projectId);
    
    List<ProjectScorePO> findByUserEmployeeId(String employeeId);
    
    List<ProjectScorePO> findByProjectId(Long projectId);
    
    Optional<ProjectScorePO> findByUserEmployeeIdAndProjectProjectName(String employeeId, String projectName);
    
    boolean existsByUserEmployeeId(String employeeId);
    
    boolean existsByProjectId(Long projectId);
} 