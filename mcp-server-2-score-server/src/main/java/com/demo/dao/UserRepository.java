package com.demo.dao;

import com.demo.entity.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserPO, Long> {
    
    Optional<UserPO> findByEmployeeId(String employeeId);
    
    boolean existsByEmployeeId(String employeeId);
} 