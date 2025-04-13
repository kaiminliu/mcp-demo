# 人员专案评分系统

## 项目简介
这是一个基于SpringBoot + JPA + MySQL5.7 + POI技术栈实现的人员专案评分系统。系统支持对员工的项目评分进行管理，包括查询、新增、删除、更新和导出等功能。系统采用前后端分离架构，提供RESTful API接口，支持JSON格式的数据交互。

## 技术栈
- SpringBoot 2.7.x
- Spring Data JPA
- MySQL 5.7
- Apache POI (用于Excel导出)
- Lombok (简化代码)
- Jackson (JSON处理)
- Maven (项目构建)
- 测试框架：JUnit 5 + Mockito

## 项目结构
```
src/main/java/com/demo/
├── common        # 公共枚举类、常量类
│   ├── RtnCode.java      # 返回码枚举
│   └── CommonResp.java   # 统一返回对象
├── config        # Spring配置类
│   └── WebConfig.java    # Web配置
├── controller    # 前端控制器
│   ├── UserController.java
│   ├── ProjectController.java
│   └── ProjectScoreController.java
├── dao           # JPA Repository持久化类
│   ├── UserRepository.java
│   ├── ProjectRepository.java
│   └── ProjectScoreRepository.java
├── dto           # 数据传输对象
│   ├── UserDTO.java      # 用户数据传输对象
│   ├── ProjectDTO.java   # 项目数据传输对象
│   └── ProjectScoreDTO.java  # 项目评分数据传输对象
├── entity        # 数据库实体类
│   ├── BasePO.java
│   ├── UserPO.java
│   ├── ProjectPO.java
│   └── ProjectScorePO.java
├── exception     # 自定义异常类
│   ├── CustomException.java
│   └── GlobalExceptionHandler.java
├── service       # 业务接口
│   └── impl      # 业务接口实现类
├── tool          # MCP服务器工具类
├── util          # 工具类
└── vo            # 视图对象
```

## 主要功能

### 1. 项目评分管理
- 查询项目评分
  - 支持通过工号、姓名、项目ID、项目名称、分数进行精确查询
  - 支持多条件组合查询
  - 支持分页查询
  - 支持无参数查询（查询所有记录）
  - 支持部分参数查询（忽略未传参数）
- 新增评分记录
  - 必填字段：工号、项目名称、分数
  - 自动填充用户和项目信息
  - 数据一致性校验
  - 用户存在性校验
  - 项目存在性校验
  - 选填字段与查询信息匹配校验
- 删除评分记录
  - 必填字段：工号、项目名称
  - 支持批量删除
  - 关联数据检查
- 更新评分记录
  - 支持更新分数
  - 支持更新关联信息
  - 必填字段：工号、项目名称
  - 用户存在性校验
  - 项目存在性校验
- 导出评分记录
  - 支持Excel格式导出
  - 支持自定义导出字段
  - 支持批量导出
  - 支持条件筛选导出

### 2. 用户管理
- 查询用户
  - 支持工号精确查询
  - 支持分页查询
  - 支持无参数查询（查询所有用户）
- 新增用户
  - 必填字段：工号、姓名
  - 工号唯一性校验
  - 用户存在性校验
- 删除用户
  - 关联数据检查
  - 支持批量删除
  - 必填字段：工号
  - 关联数据存在时禁止删除
- 更新用户
  - 支持更新姓名
  - 自动更新关联数据
  - 必填字段：工号
  - 用户存在性校验
- 导出用户
  - 支持Excel格式导出
  - 支持自定义导出字段
  - 支持条件筛选导出

### 3. 项目管理
- 查询项目
  - 支持项目名称模糊查询
  - 支持分页查询
  - 支持无参数查询（查询所有项目）
- 新增项目
  - 必填字段：项目名称
  - 项目名称唯一性校验
  - 项目存在性校验
- 删除项目
  - 关联数据检查
  - 支持批量删除
  - 必填字段：项目ID或项目名称
  - 关联数据存在时禁止删除
- 更新项目
  - 支持更新项目名称
  - 自动更新关联数据
  - 必填字段：项目ID
  - 项目存在性校验
- 导出项目
  - 支持Excel格式导出
  - 支持自定义导出字段
  - 支持条件筛选导出

## 数据库设计

### 1. 数据表结构

#### 1.1 user表
| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 | 键 |
|--------|------|------|--------|--------|------|----|
| id | bigint | 20 | 否 | 自增 | 主键 | PK |
| employee_id | varchar | 50 | 否 | 无 | 员工工号 | UK |
| employee_name | varchar | 100 | 否 | 无 | 员工姓名 |  |
| created_time | datetime |  | 否 | CURRENT_TIMESTAMP | 创建时间 |  |
| updated_time | datetime |  | 否 | CURRENT_TIMESTAMP | 更新时间 |  |

#### 1.2 project表
| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 | 键 |
|--------|------|------|--------|--------|------|----|
| id | bigint | 20 | 否 | 自增 | 主键 | PK |
| project_name | varchar | 200 | 否 | 无 | 项目名称 |  |
| created_time | datetime |  | 否 | CURRENT_TIMESTAMP | 创建时间 |  |
| updated_time | datetime |  | 否 | CURRENT_TIMESTAMP | 更新时间 |  |

#### 1.3 project_score表
| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 | 键 |
|--------|------|------|--------|--------|------|----|
| id | bigint | 20 | 否 | 自增 | 主键 | PK |
| employee_id | varchar | 50 | 否 | 无 | 员工工号 | FK |
| employee_name | varchar | 100 | 否 | 无 | 员工姓名 |  |
| project_id | bigint | 20 | 否 | 无 | 项目ID | FK |
| project_name | varchar | 200 | 否 | 无 | 项目名称 |  |
| score | decimal | 5,2 | 否 | 无 | 分数 |  |
| created_time | datetime |  | 否 | CURRENT_TIMESTAMP | 创建时间 |  |
| updated_time | datetime |  | 否 | CURRENT_TIMESTAMP | 更新时间 |  |

### 2. 表结构说明
1. 所有表都继承BasePO，包含created_time和updated_time字段
2. 表名和字段名使用小写字母加下划线命名
3. 字符集统一使用utf8mb4
4. 存储引擎使用InnoDB
5. 主键使用自增的bigint类型
6. 时间字段使用datetime类型，并设置默认值
7. 外键字段建立索引

## 接口规范

### 1. 接口命名规范
- 控制器类：`XxxController`
- 接口方法：`动词 + 名词`
- URL路径：`/api/资源名/方法名`
- 接口URL不包含字符s（如：`/api/user`）

### 2. 请求方法规范
- GET：查询操作
- POST：新增、修改、删除操作
- 查询接口：参数少用`@GetMapping`，参数多用`@PostMapping`
- 新增和修改接口：使用`@PostMapping`
- 删除接口：使用`@PostMapping`

### 3. 请求参数规范
- 查询参数：使用`@RequestParam`
- 复杂参数：使用`@RequestBody`，JSON格式
- 必填参数校验
- 参数格式校验
- 参数长度校验

### 4. 响应格式规范
```json
{
    "code": 200,
    "message": "success",
    "data": {
        // 响应数据
    }
}
```

## 错误处理

### 1. 错误码定义
- 200：成功
- 400：请求参数错误
- 401：未授权
- 403：禁止访问
- 404：资源不存在
- 500：服务器内部错误

### 2. 异常处理流程
1. 业务异常：`CustomException`
2. 参数校验异常：`MethodArgumentNotValidException`
3. 系统异常：`Exception`
4. 全局异常处理：`GlobalExceptionHandler`

### 3. 错误信息规范
- 错误信息使用中文
- 错误信息清晰明确
- 错误信息包含具体原因
- 错误信息可定位问题

## 开发环境

### 1. 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 5.7+
- IDE推荐：IntelliJ IDEA

### 2. 数据库配置
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 3. 项目配置
- 文件编码：UTF-8
- 时区设置：Asia/Shanghai
- 日志级别：INFO
- 自动建表：true
- 显示SQL：开发环境true，生产环境false

## 启动说明

### 1. 数据库初始化
1. 创建数据库：执行`src/main/resources/init.sql`文件
2. 检查数据库连接配置：确保`application.yml`中的数据库连接信息正确
   - 数据库URL：`jdbc:mysql://localhost:3306/score_system`
   - 用户名：`root`
   - 密码：`123456`

### 2. 项目启动
1. 使用IDE启动：
   - 打开项目根目录
   - 找到`McpServer2ScoreServerApplication.java`
   - 右键运行`main`方法

2. 使用Maven启动：
   ```bash
   mvn spring-boot:run
   ```

3. 打包后启动：
   ```bash
   mvn clean package
   java -jar target/mcp-server-2-score-server.jar
   ```

### 3. 访问服务
- 服务启动后，默认端口为8082
- 访问地址：`http://localhost:8082`
- 接口文档：`http://localhost:8082/swagger-ui.html`

### 4. 注意事项
1. 确保MySQL服务已启动
2. 确保数据库连接信息正确
3. 确保端口8082未被占用
4. 首次启动时会自动创建表结构
5. 开发环境建议开启SQL日志

## 注意事项

### 1. 开发规范
1. 所有实体类继承`BasePO`
2. 数据库表名和字段名使用小写字母加下划线
3. 实体类属性使用驼峰命名
4. 接口返回统一使用`CommonResp`
5. 异常处理使用`CustomException`
6. 代码注释规范
7. 日志记录规范

### 2. 安全规范
1. 敏感信息加密存储
2. 接口访问权限控制
3. 参数校验和过滤
4. SQL注入防护
5. XSS攻击防护
6. 密码加密存储
7. 敏感数据脱敏

### 3. 性能优化
1. 合理使用索引
2. 批量操作优化
3. 缓存使用
4. 分页查询优化
5. 大文件导出优化
6. 数据库连接池配置
7. JVM参数优化

### 4. 数据一致性
1. 事务管理
2. 数据同步
3. 关联数据更新
4. 数据备份
5. 数据恢复

### 5. 测试规范
1. 单元测试
2. 接口测试
3. 性能测试
4. 安全测试
5. 兼容性测试

## 数据传输对象

### 1. UserDTO
```java
public class UserDTO {
    private Long id;                    // 主键ID
    private String employeeId;          // 员工工号
    private String employeeName;        // 员工姓名
    private LocalDateTime createdTime;  // 创建时间
    private LocalDateTime updatedTime;  // 更新时间
}
```

### 2. ProjectDTO
```java
public class ProjectDTO {
    private Long id;                    // 主键ID
    private String projectName;         // 项目名称
    private LocalDateTime createdTime;  // 创建时间
    private LocalDateTime updatedTime;  // 更新时间
}
```

### 3. ProjectScoreDTO
```java
public class ProjectScoreDTO {
    private Long id;                    // 主键ID
    private String employeeId;          // 员工工号
    private String employeeName;        // 员工姓名
    private Long projectId;             // 项目ID
    private String projectName;         // 项目名称
    private BigDecimal score;           // 分数
    private LocalDateTime createdTime;  // 创建时间
    private LocalDateTime updatedTime;  // 更新时间
}
```

## 测试说明
### 单元测试
1. 控制器测试
   - 使用MockMvc模拟HTTP请求
   - 验证请求参数和响应结果
   - 测试异常情况处理

2. 服务层测试
   - 使用Mockito模拟依赖
   - 验证业务逻辑
   - 测试异常情况处理

3. 工具类测试
   - 测试工具方法的正确性
   - 验证异常处理

### 测试用例覆盖
1. 用户管理测试
   - 创建用户（成功/已存在）
   - 查询用户（带参数/不带参数）
   - 删除用户（成功/不存在）
   - 更新用户名（成功/不存在）
   - 导出用户列表（成功/失败）

2. 项目管理测试
   - 创建项目（成功/已存在）
   - 查询项目（带参数/不带参数）
   - 删除项目（成功/不存在）
   - 更新项目（成功/不存在）
   - 导出项目列表（成功/失败）

3. 评分管理测试
   - 创建评分（成功/已存在）
   - 查询评分（带参数/不带参数）
   - 删除评分（成功/不存在）
   - 更新评分（成功/不存在）
   - 导出评分列表（成功/失败）
