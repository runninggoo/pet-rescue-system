  ## 📋 创建claude.md文件

  ```markdown
  # 宠物救助收养系统 - 项目规范

  ## 🎯 项目概述

  宠物救助收养系统是一个基于Spring Boot +
  Vue.js的前后端分离Web应用，旨在解决上海地区流浪宠物救助收养领域的数字化管理问题。系统覆盖从"救助发
  现→领养审核→健康管理→后续跟踪"的全业务流程，支持多角色在线协同工作。

  ## 🛠️ 技术栈

  ### 后端技术栈
  - **框架**：Spring Boot 2.7.0
  - **安全认证**：Spring Security + JWT
  - **持久层**：MyBatis Plus 3.5.3.1
  - **数据库**：MySQL 8.0+
  - **其他**：Lombok、Apache Commons、JJWT

  ### 前端技术栈
  - **框架**：Vue.js 2.6.14
  - **UI组件**：Element UI
  - **HTTP库**：Axios
  - **图表库**：ECharts（可选）

  ## 📁 文件结构

  ### 后端结构
  backend/
  ├── src/main/java/com/pet/rescue/
  │   ├── config/          # 配置类
  │   ├── controller/      # 控制器层
  │   ├── entity/          # 实体类
  │   ├── mapper/          # Mapper接口
  │   ├── service/         # 服务层
  │   │   ├── impl/        # 服务实现
  │   ├── security/        # 安全认证
  │   ├── utils/           # 工具类
  │   ├── vo/              # 值对象
  │   └── PetRescueSystemApplication.java  # 启动类
  ├── src/main/resources/
  │   ├── application.yml  # 配置文件
  │   └── mapper/          # MyBatis映射文件
  └── pom.xml              # Maven配置

  ### 前端结构
  frontend/
  ├── public/              # 静态页面
  ├── src/
  │   ├── api/             # API接口
  │   ├── assets/          # 静态资源
  │   ├── components/      # Vue组件
  │   ├── router/          # 路由配置
  │   ├── store/           # 状态管理
  │   └── utils/           # 工具函数
  └── package.json         # Node依赖

  ## 🔧 开发规范

  ### 代码风格
  - **Java命名**：
    - 类名：PascalCase（如`PetController`）
    - 方法名：camelCase（如`findPetDetail`）
    - 变量名：camelCase（如`petId`）
    - 常量名：UPPER_SNAKE_CASE（如`DEFAULT_STATUS`）

  - **Vue命名**：
    - 组件名：PascalCase（如`PetList`）
    - 方法名：camelCase（如`getPetList`）
    - 属性名：camelCase（如`petInfo`）

  ### 注释规范
  - **Java**：所有公共方法必须有Javadoc注释
  - **Vue**：复杂组件必须有详细注释
  - **SQL**：关键SQL语句必须有注释

  ### 异常处理
  - 所有Controller必须有try-catch块
  - 使用统一的错误返回格式
  - 记录详细的错误日志

  ### API设计
  - RESTful风格
  - 统一的返回格式：`ResponseResult`
  - 正确的HTTP状态码
  - 详细的API文档

  ## 🎯 核心功能

  ### 1. 救助所管理系统
  - 救助所CRUD功能
  - 上海区域数据管理
  - 智能匹配算法
  - 距离计算和评分

  ### 2. 健康档案系统
  - 疫苗记录管理
  - 体检记录追踪
  - 治疗历史记录
  - 医疗资源管理

  ### 3. 协同任务管理
  - 任务发布系统
  - 志愿者接单机制
  - 任务进度跟踪
  - 多角色协同

  ### 4. 数据统计系统
  - 救助数量统计
  - 领养成功率分析
  - 区域分布统计
  - 季节性分析

  ## 🧪 开发指南

  ### 环境要求
  - JDK 1.8+
  - MySQL 8.0+
  - Node.js 14+
  - Maven 3.6+

  ### 启动步骤
  1. **数据库准备**：
     ```bash
     mysql -u root -p < sql/schema.sql
     mysql -u root -p < sql/test_data.sql

  2. 后端启动：
  cd backend
  mvn clean install
  mvn spring-boot:run
  3. 前端启动：
  cd frontend
  npm install
  npm run dev

  开发流程

  1. 需求分析 → 2. 数据库设计 → 3. 后端开发 → 4. 前端开发 → 5. 联调测试 → 6. 优化部署

  📊 测试指南

  单元测试

  - Service层方法测试
  - Controller层接口测试
  - Mapper层SQL测试

  集成测试

  - 用户登录流程
  - 救助所查询流程
  - 智能匹配算法
  - 权限控制测试

  系统测试

  - 完整业务流程测试
  - 性能测试
  - 安全测试

  🚀 部署指南

  服务器要求

  - CPU：2核以上
  - 内存：4GB以上
  - 硬盘：50GB以上
  - 系统：Linux/Windows Server

  部署步骤

  1. 打包项目：mvn clean package
  2. 配置服务器环境
  3. 部署后端JAR包
  4. 部署前端静态文件
  5. 配置Nginx反向代理
  6. 启动服务并测试

  📚 维护指南

  代码维护

  - 定期代码审查
  - 及时修复bug
  - 优化性能
  - 更新依赖

  数据维护

  - 定期备份数据库
  - 清理无用数据
  - 优化表结构
  - 监控数据增长

  系统监控

  - 监控服务状态
  - 监控系统性能
  - 监控用户行为
  - 监控错误日志

  🎓 毕业设计说明

  本项目作为上海大学计算机专业毕业设计，旨在：
  - 探索"技术+公益"的融合模式
  - 解决宠物救助收养领域的数字化痛点
  - 提供可运行的数字化解决方案原型
  - 为同类社会公益平台提供参考案例

  📞 联系方式

  如有任何问题，欢迎提交Issue或联系项目维护者。

  ---最后更新：2026-03-31
  版本：1.0.0
  状态：开发中

  ## 🎯 创建完成