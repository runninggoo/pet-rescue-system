# 宠物救助收养系统 - 部署指南

## 环境要求

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 1.8+ | 后端运行环境 |
| MySQL | 5.7+ / 8.0+ | 数据库 |
| Node.js | 14+ | 前端开发环境 |
| Maven | 3.6+ | 后端构建工具 |

## 数据库配置

### 1. 创建数据库

```sql
CREATE DATABASE pet_rescue CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pet_rescue;
```

### 2. 执行建表脚本

```bash
mysql -u root -p pet_rescue < sql/schema.sql
mysql -u root -p pet_rescue < sql/pet_category.sql
mysql -u root -p pet_rescue < sql/refresh_token.sql
mysql -u root -p pet_rescue < sql/user_pet_behavior.sql
mysql -u root -p pet_rescue < sql/test_data.sql
```

## 后端配置

### 1. 复制配置文件

将 `application-example.yml` 复制为 `application.yml`：

```bash
cp backend/src/main/resources/application-example.yml backend/src/main/resources/application.yml
```

### 2. 修改数据库密码

编辑 `application.yml`，修改数据库密码：

```yaml
spring:
  datasource:
    password: your_password_here  # 修改为你的MySQL密码
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

或者使用 IDE 直接运行 `PetRescueSystemApplication.java`。

后端启动后，访问地址：`http://localhost:8081/api`

## 前端配置

### 1. 安装依赖

```bash
cd frontend
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

前端启动后，访问地址：`http://localhost:5173`

访问后会自动跳转到登录页面。

### 3. 直接访问静态页面（可选）

也可以直接打开 `frontend/public/login.html`，但需要确保后端服务已启动。

## 测试账号

| 角色 | 手机号 | 密码 |
|------|--------|------|
| 管理员 | 13800000000 | 123456 |
| 领养人 | 13900000001 | 123456 |
| 志愿者 | 13600000001 | 123456 |

## 常见问题

### 1. 数据库连接失败

- 检查 MySQL 服务是否启动
- 检查 `application.yml` 中的数据库密码是否正确
- 检查数据库 `pet_rescue` 是否已创建

### 2. 前端页面空白

- 确保后端服务已启动
- 检查浏览器控制台是否有错误
- 尝试清除浏览器缓存后刷新

### 3. 登录失败

- 检查测试账号是否正确
- 检查数据库中是否有测试数据

## 项目结构说明

```
pet-rescue-system/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/         # Java 源码
│   │   └── com/pet/rescue/
│   │       ├── config/        # 配置类
│   │       ├── controller/    # 控制器
│   │       ├── entity/         # 实体类
│   │       ├── mapper/         # Mapper接口
│   │       ├── service/       # 业务逻辑
│   │       ├── security/       # 安全认证
│   │       ├── utils/          # 工具类
│   │       └── vo/             # 视图对象
│   └── src/main/resources/
│       ├── application.yml     # 配置文件（需配置）
│       ├── mapper/             # Mapper XML
│       └── static/             # 前端静态文件
│
├── frontend/                   # Vue.js 前端
│   ├── public/                 # 静态页面
│   │   ├── js/                # 公共JS
│   │   └── *.html             # HTML页面
│   └── src/                   # Vue源码
│
├── sql/                       # 数据库脚本
│   ├── schema.sql            # 建表脚本
│   ├── test_data.sql        # 测试数据
│   └── ...
│
└── figures/                  # 项目图表
    ├── ER图_简化版.html      # E-R关系图
    └── 架构图_前后端分离.html # 架构图
```

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 2.7.0 + Spring Security + MyBatis Plus |
| 前端 | Vue.js 2.6.14 + Element UI 2.15.13 |
| 数据库 | MySQL 8.0 |
| 认证 | JWT + Refresh Token |
