package com.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "project_score")
@Getter
@Setter
public class ProjectScorePO extends BasePO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", nullable = false)
    private UserPO user;
    
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectPO project;
    
    @Column(name = "score", nullable = false)
    private Double score;
} 