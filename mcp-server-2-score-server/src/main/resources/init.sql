-- 创建数据库
CREATE DATABASE IF NOT EXISTS score_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE score_system;

-- 创建用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL UNIQUE COMMENT '工号',
    employee_name VARCHAR(50) NOT NULL COMMENT '姓名',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建项目表
CREATE TABLE IF NOT EXISTS project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- 创建项目评分表
CREATE TABLE IF NOT EXISTS project_score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL COMMENT '项目ID',
    employee_id VARCHAR(20) NOT NULL COMMENT '工号',
    score DECIMAL(5,2) NOT NULL COMMENT '评分',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (project_id) REFERENCES project(id),
    FOREIGN KEY (employee_id) REFERENCES user(employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目评分表';

-- 插入用户数据
INSERT INTO user (employee_id, employee_name) VALUES
('W0000001', '张三'),
('W0000002', '李四'),
('W0000003', '王五'),
('W0000004', '赵六');

-- 插入项目数据
INSERT INTO project (project_name) VALUES
('Project 1'),
('Project 2'),
('Project 3');

-- 插入项目评分数据
INSERT INTO project_score (project_id, employee_id, score) VALUES
(1, 'W0000001', 80),
(1, 'W0000002', 30),
(1, 'W0000003', 68),
(1, 'W0000004', 75),
(2, 'W0000001', 80),
(2, 'W0000002', 45),
(2, 'W0000003', 90),
(2, 'W0000004', 30),
(3, 'W0000001', 100),
(3, 'W0000002', 0); 