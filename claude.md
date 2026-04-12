# 宠物救助收养系统 - 项目规范

## 🎯 项目概述

宠物救助收养系统是一个基于Spring Boot + Vue.js的前后端分离Web应用，旨在解决上海地区流浪宠物救助收养领域的数字化管理问题。系统覆盖从"救助发现→领养审核→健康管理→后续跟踪"的全业务流程，支持多角色在线协同工作。

## 🛠️ 技术栈

  ### 后端技术栈
  - **框架**：Spring Boot 2.7.0
  - **安全认证**：Spring Security + JWT
  - **持久层**：MyBatis Plus 3.5.3.1
  - **数据库**：MySQL 8.0+
  - **其他**：Lombok、Apache Commons、JJWT

  ### 前端技术栈
  - **框架**：Vue.js 2.6.14
  - **UI组件**：Element UI 2.15.13
  - **HTTP库**：Axios 0.27.2
  - **构建工具**：Vite（开发服务器）
  - **图表库**：ECharts（可选）

  ### ⚠️ 重要前端开发规范（必须遵守）

  #### 1. CDN 链接必须带版本号
  所有第三方库 CDN 必须指定版本，**禁止省略版本号**：

  ```html
  <!-- ✅ 正确 -->
  <script src="https://unpkg.com/element-ui@2.15.13/lib/index.js"></script>
  <link rel="stylesheet" href="https://unpkg.com/element-ui@2.15.13/lib/theme-chalk/index.css">

  <!-- ❌ 错误（会加载不兼容版本） -->
  <script src="https://unpkg.com/element-ui/lib/index.js"></script>
  ```

  #### 2. Element UI 必须全局注册
  Vue 脚本引入后，必须调用 `Vue.use(ELEMENT)` ：

  ```html
  <script src="https://cdn.jsdelivr.net/npm/vue@2.6.14/dist/vue.js"></script>
  <script src="https://unpkg.com/element-ui@2.15.13/lib/index.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/axios@0.27.2/dist/axios.min.js"></script>
  <script>
      Vue.use(ELEMENT);   // ← 必须这一行！
      // ... 其他代码
  </script>
  ```

  #### 3. 禁止使用 axios.defaults.baseURL
  所有 API 调用必须使用**完整相对路径**，在每个 axios 调用中直接写 `/api/` 前缀：

  ```javascript
  // ✅ 正确（完整路径）
  axios.get('/api/pet/list', { params: { page: 1, pageSize: 100 } })
  axios.post('/api/auth/login', { phone: '13800000000', password: '123456' })
  axios.delete('/api/health-record/delete/' + id)

  // ❌ 错误（禁止 baseURL）
  axios.defaults.baseURL = '/api';   // 已在 Axios 1.x 中废弃！
  axios.get('/pet/list');              // → 变成 /api/api/pet/list (404)
  ```

  原因：`axios.defaults.baseURL` 在 Axios 1.x+ 中与相对路径解析有 bug，导致路径重复拼接。

  #### 4. Vue 组件必须放在 #app 内
  所有对话框（el-dialog）必须放在 `<div id="app">` 内部：

  ```html
  <body>
    <div id="app">
      <!-- 页面主体 -->
      <el-dialog>...</el-dialog>   ← 必须在 #app 内
    </div><!-- end #app -->
    <script>new Vue({ el: '#app' })</script>
  </body>
  ```

  否则对话框内的 Vue 模板语法不会被渲染，直接显示 `{{ expression }}`。

  #### 5. ⚠️ el-dialog 的 Vue 隔离问题（重要，2026-04-02 新增）
  Element UI 的 `el-dialog` 会在 `#app` 外部生成 `el-dialog__wrapper` 并挂载到 `document.body`，
  同时创建独立的 Vue 组件实例上下文。这导致 Dialog 内所有 `@click` 事件回调无法访问
  根 `#app` Vue 实例的方法，所有按钮点击完全失效。

  **诊断方法**：
  ```javascript
  // 从按钮向上找最近的 Vue 实例（应找到根 app 的 79 个属性）
  // 如果只有 18 个属性，说明是 el-dialog 的隔离实例
  let el = document.querySelector('.cat-left .el-button');
  while (el = el.parentElement) { if (el.__vue__) console.log(Object.keys(el.__vue__).length); }
  ```

  **解决方案**：对于需要大量交互的复杂弹窗，使用原生 div 覆盖层替代 el-dialog：

  ```html
  <!-- 原生覆盖层：所有事件都在根 Vue 实例内 -->
  <div v-if="showDialog" class="cat-overlay" @click.self="showDialog = false">
      <div class="cat-panel">
          <div class="cat-panel-header">
              <h3>标题</h3>
              <span @click="showDialog = false">✕</span>
          </div>
          <div class="cat-panel-body">
              <!-- 所有 @click 均可正常工作 -->
              <el-button @click="handleSubmit">提交</el-button>
          </div>
      </div>
  </div>
  ```

  **CSS 覆盖层样式参考**：
  ```css
  .cat-overlay {
      position: fixed; inset: 0; z-index: 2000;
      background: rgba(61, 64, 91, 0.5); backdrop-filter: blur(4px);
      display: flex; align-items: center; justify-content: center;
  }
  .cat-panel {
      background: white; border-radius: 16px; width: 700px; max-height: 85vh;
      display: flex; flex-direction: column; overflow: hidden;
  }
  ```

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

  ### 5. 志愿者成长体系（v1.1.0，2026-04-03）
  - 积分档案：累计积分/可用积分/当前等级/等级进度
  - 等级系统：6级成长体系（新手上路 → 公益之星）
  - 每日签到：每日+5分，连续7天额外+20分
  - 积分历史：收支明细、来源分类、分页浏览
  - 排行榜：TOP10 志愿者排名
  - 积分动画：完成任务时弹出积分获得横幅和升级提示
  - 入口：profile.html「成长中心」tab（志愿者专属）+ volunteer-task.html 横幅

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

 **⚠️ 重要：前端文件同步规范**
 Spring Boot 的 static 资源目录（`backend/src/main/resources/static/`）与 Vite 开发目录（`frontend/public/`）是**两套独立的 HTML 文件**。
 - `frontend/public/`：由 Vite 开发服务器（端口 5173）直接提供，实时反映最新代码
 - `backend/src/main/resources/static/`：由 Spring Boot（端口 8081）直接提供，仅在 `mvn clean install` 时从源码复制

 **每次修改前端 HTML 文件后，必须手动同步到后端 static 目录**（否则通过 8081 端口访问到的仍是旧版）：
 ```powershell
 # PowerShell 同步所有前端文件到后端 static 目录
 $src = "C:\Users\33169\Desktop\计算机\毕业设计\项目\frontend\public"
 $dst = "C:\Users\33169\Desktop\计算机\毕业设计\项目\backend\src\main\resources\static"
 Get-ChildItem "$src\*.html" | ForEach-Object { Copy-Item "$src\$($_.Name)" "$dst\$($_.Name)" -Force }
 ```
 重启后端服务后生效。

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

---最后更新：2026-04-12（v1.2.1 宠物收藏功能开发 + GitHub v1.2推送 + 文档全面更新）
版本：1.2.1
状态：v1.2.0 UI优化全部完成 ✅ | 宠物收藏功能 ✅ | GitHub v1.2推送 ✅ | 文档全面更新 ✅
新增：v1.2.1 宠物收藏功能（Entity/Mapper/Service/Controller）；截图管理文档v1.2.0（44张）；论文素材保存文档v1.3（JWT双Token/成长体系）

  ## 🎨 前端 UI 设计规范（2026-04-02 新增）

  ### 设计方向：温馨治愈宠物风

  所有前端页面必须遵循统一的温宠物风格，与其他页面保持一致。

  #### 色彩体系（必须使用 CSS 变量）

  ```css
  :root {
      --primary: #E07A5F;       /* 陶土珊瑚 — 温暖治愈感，主色调 */
      --primary-dark: #c96a50; /* 深珊瑚 */
      --primary-light: #f0a28a; /* 浅珊瑚 */
      --primary-bg: rgba(224, 122, 95, 0.08);
      --sage: #81B29A;          /* 薄荷绿 — 健康清新 */
      --sage-light: #a8cdb8;
      --amber: #F4A261;         /* 琥珀橙 — 紧急/重要 */
      --warm-white: #FDF6F0;    /* 暖白 — 奶油质感背景 */
      --warm-cream: #FAEEE7;    /* 浅裸粉 — 卡片底色 */
      --warm-gray: #f0ebe7;
      --warm-gray2: #ede6df;
      --text-dark: #3D405B;     /* 深灰紫 — 柔和文字 */
      --text-muted: #8A8EA0;    /* 浅灰 — 次要文字 */
      --bg: var(--warm-white);
      --card-shadow: 0 4px 20px rgba(224, 122, 95, 0.1);
      --card-shadow-hover: 0 12px 40px rgba(224, 122, 95, 0.2);
      --transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
      --radius: 20px;           /* 大圆角：主卡片 */
      --radius-sm: 12px;        /* 小圆角：按钮/输入框 */
  }
  ```

  #### 字体

  - 英文：Nunito（圆润可爱） — `<link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet">`
  - 中文：PingFang SC / Microsoft YaHei

  #### 页面风格分组

  | 类别 | 页面 | 状态 |
  |------|------|------|
  | 标准温宠物风格（完整 `:root` + Nunito） | login, register, pet-list, adoption-list, pet-detail, health-record, volunteer-task, stats, profile, shelter-recommendation | ✅ 统一 |
  | 原始简陋风格（待改造） | user-management.html, simple-stats.html | ❌ 需 P0 改造 |

  **改造要求**：user-management.html 和 simple-stats.html 必须引入完整 CSS 变量、Nunito 字体、Element UI 覆盖样式。

  #### 页面 CSS 结构模板（新页面必须复制此结构）

  ```html
  <head>
      <link rel="stylesheet" href="https://unpkg.com/element-ui@2.15.13/lib/theme-chalk/index.css">
      <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet">
      <style>
          :root {
              --primary: #E07A5F; --primary-dark: #c96a50; --primary-light: #f0a28a;
              --primary-bg: rgba(224, 122, 95, 0.08);
              --sage: #81B29A; --sage-light: #a8cdb8;
              --amber: #F4A261;
              --warm-white: #FDF6F0; --warm-cream: #FAEEE7;
              --warm-gray: #f0ebe7; --warm-gray2: #ede6df;
              --text-dark: #3D405B; --text-muted: #8A8EA0;
              --bg: var(--warm-white);
              --card-shadow: 0 4px 20px rgba(224, 122, 95, 0.1);
              --card-shadow-hover: 0 12px 40px rgba(224, 122, 95, 0.2);
              --transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
              --radius: 20px; --radius-sm: 12px;
          }
          * { box-sizing: border-box; }
          *:focus-visible { outline: 2px solid var(--primary); outline-offset: 2px; }
          body { margin: 0; padding: 0; background: var(--bg); font-family: "Nunito", "PingFang SC", "Microsoft YaHei", sans-serif; color: var(--text-dark); }
          ::-webkit-scrollbar { width: 6px; height: 6px; }
          ::-webkit-scrollbar-track { background: var(--warm-gray); border-radius: 3px; }
          ::-webkit-scrollbar-thumb { background: var(--primary-light); border-radius: 3px; }
          ::-webkit-scrollbar-thumb:hover { background: var(--primary); }
          .el-button--primary { background-color: var(--primary) !important; border-color: var(--primary) !important; border-radius: var(--radius-sm) !important; }
          .el-button--primary:hover { background-color: var(--primary-dark) !important; border-color: var(--primary-dark) !important; transform: translateY(-1px); box-shadow: 0 4px 12px rgba(224, 122, 95, 0.3); }
          .el-button--success { background-color: var(--sage) !important; border-color: var(--sage) !important; border-radius: var(--radius-sm) !important; }
          .el-button--warning { background-color: var(--amber) !important; border-color: var(--amber) !important; border-radius: var(--radius-sm) !important; }
          .el-input__inner:focus { border-color: var(--primary) !important; box-shadow: 0 0 0 3px rgba(224,122,95,0.12) !important; }
          .el-pager li.active { background-color: var(--primary) !important; color: white !important; border-radius: 50% !important; }
          .el-pagination .el-pager li:hover { color: var(--primary) !important; }
      </style>
  </head>
  ```

  ## ⚠️ MyBatis Plus 逻辑删除重要注意事项

  ### 2026-04-02 更新（已修复）

  **问题**：application.yml 全局逻辑删除配置与 @TableLogic 注解同时存在，加上数据库 deleted 列为 NULL 而非 0，
  导致 pet 列表分页 COUNT=0，total=0，翻页时内容不变。

  **修复方案**（三步）：
  1. 数据库：UPDATE SET deleted=0 WHERE deleted IS NULL
  2. 配置文件：移除 application.yml 全局逻辑删除配置
  3. 分页逻辑：PetServiceImpl 放弃自动 COUNT，改为手动 selectCount + Java 层分页截取

  ### 规范（必须遵守）

  所有实体类的逻辑删除必须通过 @TableLogic 注解实现，不要依赖 application.yml 全局配置。
  新增表时必须同步在数据库表和实体类中添加 deleted 字段。

  ## ⚠️ MyBatis Plus isNull/isNotNull 极易写反（2026-04-03 新增）

  **问题**：`PetCategoryServiceImpl.getChildren(Long parentId)` 中误用 `isNull(PetCategory::getParentId)` ，
  导致查出的是根节点而非子节点，所有大类的子品种全部混在一起。

  **区分**：
  - `parent_id IS NULL` → 根节点（一级分类，如"猫"、"狗"）
  - `parent_id IS NOT NULL` → 子节点（品种，如"中华田园猫"）

  **教训**：涉及父子关系的邻接表查询时，写完条件后反向验证一遍（"IS NULL = 父节点，对吗？"）。

  ## ⚠️ 品种分类 API 安全配置（2026-04-03 新增）

  **问题**：`SecurityConfig.java` 未将 `/api/category/**` 加入 permitAll，
  导致品种列表 API 返回 403，宠物列表页无法正常加载品种下拉。

  **规范**：`pet_category` 表是公开只读数据，`/api/category/**` 必须在 SecurityConfig 中设为 permitAll，
  且品种下拉在宠物列表页（无需登录）中使用。

  ```java
  .antMatchers("/category/**").permitAll()   // 品种分类无需认证
  ```

  ## ⚠️ 宠物大类筛选 categoryType 兼容 NULL 查询（2026-04-03 新增）

  **问题**：数据库中部分宠物记录的 `category_type` 为 NULL，若用精确 `eq(categoryType)` 过滤会漏掉这些记录。

  **修复**：`PetServiceImpl.findPetsByCondition` 中，categoryType 查询改为：
  ```java
  queryWrapper.and(w -> w.eq(Pet::getCategoryType, categoryType).or().isNull(Pet::getCategoryType));
  ```

  选"猫"时同时返回 `category_type='CATS'` 和 `category_type=NULL` 的宠物，保证数据完整性。

  ## ⚠️ 志愿者任务统计必须过滤 volunteerId（2026-04-04 新增）

  **问题**：`VolunteerTaskServiceImpl.getStats(Long userId)` 中 `userId` 参数接收但从未使用，
  四个 `selectCount` 均为全系统统计，导致个人中心"我的任务"统计与任务列表数据完全矛盾。

  **区分**：
  - `/api/volunteer-task/stats` → 当前登录用户的任务统计（按 volunteerId 过滤）
  - `/api/volunteer-task/my-tasks` → 当前登录用户的任务列表（按 volunteerId 过滤）

  所有统计类接口必须和列表接口保持相同的 volunteerId 过滤条件，否则统计数字和列表对不上。

  ```java
  // ✅ 正确：所有计数都加 volunteerId 过滤
  LambdaQueryWrapper<VolunteerTask> baseWrapper =
      new LambdaQueryWrapper<VolunteerTask>()
          .eq(VolunteerTask::getVolunteerId, userId);
  long total = baseMapper.selectCount(baseWrapper);

  // ❌ 错误：selectCount(null) = 全系统统计，与列表不符
  long total = baseMapper.selectCount(null);
  ```

  ## 🎯 更新完成（v1.1.2）

  ---

  ## 📌 v1.1.3 更新（2026-04-11）UI全面优化

  ### 背景
  2026-04-11 对9个前端页面进行了系统化UI全面优化，修复了CSS变量不一致、布局缺陷、Vue挂载bug等问题。

  ### 阶段一：修复破坏性 bug（P0）

  | 文件 | 问题 | 修复内容 |
  |------|------|---------|
  | `pet-list.html` | `--radius: 16px`，缺少4个变量 | 改为 `--radius: 20px`，补全 `--primary-bg`/`--bg`/`--warm-gray2` |
  | `pet-detail.html` | 变量名不一致（`--border-radius`） | 统一为 `--radius`，补全缺失变量 |
  | `shelter-recommendation.html` | 重复变量 `--shadow` | 删除重复，与 `--card-shadow` 合并 |
  | `pet-list.html` | 统计卡片 `auto-fit` 挤压网格区域 | 改为 `grid-template-columns: repeat(4, 1fr)` 固定4列 |
  | `pet-list.html` | 宠物网格 `auto-fit` 最后一行不撑满 | 改为 `auto-fill` + `minmax(260px, 1fr)` |

  ### 阶段二：统一 Element UI 覆盖（P1）

  为 `pet-list.html` 和 `pet-detail.html` 补全了以下覆盖样式：
  - 爪印 SVG 装饰背景（`.page-bg-paws`）
  - 页面包装容器（`.page-wrapper`，`position: relative; z-index: 1`）
  - 分页器圆形激活状态
  - 加载遮罩透明度
  - 对话框/消息框/标签统一圆角
  - 表格描述表头暖色背景

  ### 阶段三：消除 Vue 隔离风险（P2）

  **`user-management.html` 重大修复**：
  - 该文件**原本缺少 `<div id="app">`**，导致 Vue 实例从未挂载，所有 axios 请求无法触发，用户列表无法加载
  - 修复：添加 `<div id="app" class="page-wrapper">` 包裹内容
  - 同时将 `el-dialog` 替换为**原生 div 遮罩层**（符合 CLAUDE.md 规范，避免 Vue 隔离问题）
  - 表单验证从 `$refs.userForm.validate()` 改为手动 JS 校验

  **`health-record.html` / `volunteer-task.html`**（第一轮标记为无需改动，第二轮已修复）：
  - 第一轮：已有 `<div id="app">`，`el-dialog` 当时未处理
  - 第二轮（2026-04-11 下午）：详情弹窗 + 添加/编辑/发布/完成任务弹窗全部改为原生 div 遮罩层
  - 表单校验全部改为手动 JS 校验，状态标签从 `<el-tag>` 改为原生 `.status-inline span`

  ### 阶段四：视觉细节打磨（P3）

  **`pet-list.html` 横幅导航精简**：
  - 原9个导航链接（用户管理/领养审核/数据统计/健康档案/任务管理等）全部平铺 → 改为常驻3个（个人中心/救助站/帮助）+ 1个「管理 ▼」下拉菜单
  - 横幅右侧冗余统计徽章（待领养/已领养数字，与下方 stats-bar 重复）已删除

  **`pet-list.html` 管理下拉菜单**：
  - 点击「管理 ▼」展开白色下拉面板（用户管理/领养审核/数据统计/健康档案/任务管理）
  - 点击任意项跳转并关闭菜单，点击外部自动关闭（`document.addEventListener('click')`）
  - 角色控制：仅 `admin`/`institution_admin`/`volunteer` 可见

  ### 数据库修复

  **`follow_up_record` 表结构不完整**：
  - 原表只有：`id`, `adoption_id`, `user_id`, `status`, `feedback`, `created_time`, `updated_time`, `deleted`
  - 缺少：`follow_up_date`, `follower_id`, `pet_condition`, `content`, `images`
  - 修复：`ALTER TABLE` 添加了所有缺失字段
  - 测试数据：申请 `app_id=29`（宠物旺财）已有2条回访记录（2026-04-05、2026-04-12）

  ### 新增 CSS 规范

  所有页面必须使用统一 CSS 变量体系（不得出现例外）：

  ```css
  :root {
      --primary: #E07A5F;        --primary-dark: #c96a50;  --primary-light: #f0a28a;
      --primary-bg: rgba(224, 122, 95, 0.08);
      --sage: #81B29A;            --sage-light: #a8cdb8;
      --amber: #F4A261;
      --warm-white: #FDF6F0;      --warm-cream: #FAEEE7;
      --warm-gray: #f0ebe7;      --warm-gray2: #ede6df;
      --text-dark: #3D405B;       --text-muted: #8A8EA0;
      --bg: var(--warm-white);
      --card-shadow: 0 4px 20px rgba(224, 122, 95, 0.10);
      --card-shadow-hover: 0 12px 40px rgba(224, 122, 95, 0.20);
      --transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
      --radius: 20px;             --radius-sm: 12px;
  }
  ```

  ### 新增页面结构模板

  所有页面 HTML 结构模板（必须遵循）：

  ```html
  <body>
  <!-- 爪印装饰背景（可选） -->
  <div class="page-bg-paws">
      <svg ... viewBox="0 0 24 24"><path d="..."/></svg>
      <!-- 可放置多个爪印 SVG -->
  </div>
  <!-- Vue 挂载根（必须 id="app"） -->
  <div id="app" class="page-wrapper">
      <!-- 页面内容 -->
  </div>
  <script src="...vue.js"><script>
  <script src="...element-ui/index.js"><script>
  <script src="...axios.min.js"><script>
  <script>
      Vue.use(ELEMENT);
      new Vue({ el: '#app', ... })
  </script>
  ```

  ### 回访调查功能入口

  位置：领养审核 → 已完成标签 → 点击任意申请行（如旺财） → 右侧抽屉 → 「回访记录」Tab
  - 添加按钮（管理员/机构管理员可见）
  - 时间线展示已有回访记录
  - API: `GET /api/follow-up/list/{adoptionId}`, `POST /api/follow-up/add`

  ### 第二轮优化补充（2026-04-11 下午）

  **`stats.html` 骨架屏 HTML 结构**：
  - 加载时显示 6 张统计卡骨架 + 2 个图表骨架（CSS 已定义但 HTML 未使用，现已完整添加）
  - 修复 `startPlaceholder`/`endPlaceholder` 日期选择器占位符显示

  **`shelter-recommendation.html` CSS 变量补充**：
  - 补充 `--shadow`/`--shadow-hover` 变量，避免 CSS 引用未定义变量报错

  **`pet-detail.html` 健康档案可见性优化**：
  - 健康档案卡片可见条件从 `pet.status === 0` 改为 `isLoggedIn`
  - 所有登录角色均可查看宠物健康档案

  **`adoption-list.html` 原生遮罩层**：
  - 详情抽屉已使用 `.custom-modal-overlay` 原生遮罩层（`el-drawer` 样式自定义）
  - 回访记录表单使用独立 `.custom-modal-overlay` 遮罩层

  **`profile.html` / `simple-stats.html` / `help.html`**：
  - 三个页面均已具备 `.page-bg-paws`、`.page-wrapper`、完整 CSS 变量体系

  ## 🎯 更新完成（v1.1.3）

  ---

  ## 📌 v1.1.4 更新（2026-04-11）JWT Token 刷新功能完全落地

  ### 背景

  后端 `AuthController` + `RefreshTokenService` + `refresh_token` 表的完整实现早已存在，
  但前端 `auth.js` 仅在 `login.html` 和 `register.html` 中引入，其余 11 个页面全部缺失，
  导致 Access Token 过期后**不会自动刷新**，用户体验中断。

  ### 修复内容

  **前端 - 所有需要认证的页面均已补充引入 `auth.js`**：

  | 页面 | 引入位置 |
  |------|---------|
  | `login.html` | ✅ 原本已有 |
  | `register.html` | ✅ 原本已有 |
  | `pet-list.html` | ✅ 新增 |
  | `pet-detail.html` | ✅ 新增 |
  | `adoption-list.html` | ✅ 新增 |
  | `health-record.html` | ✅ 新增 |
  | `volunteer-task.html` | ✅ 新增 |
  | `stats.html` | ✅ 新增 |
  | `profile.html` | ✅ 新增 |
  | `user-management.html` | ✅ 新增 |
  | `shelter-recommendation.html` | ✅ 新增 |
  | `pet-hospital.html` | ✅ 新增 |
  | `simple-stats.html` | ✅ 新增 |

  所有文件已同步到 `backend/src/main/resources/static/` 目录。

  ### JWT Token 刷新机制说明（论文可用）

  **双 Token 架构**：
  - Access Token（JWT）：短期（30分钟），存 localStorage + Cookie，每次 API 请求携带
  - Refresh Token：长期（7天），存 localStorage + 数据库，用于 Access Token 过期后刷新

  **工作流程**：
  1. 登录时后端生成双 Token，返回前端
  2. 每次 API 请求，axios 请求拦截器自动在 `Authorization: Bearer <token>` 中附加 Access Token
  3. Access Token 过期时后端返回 401，前端 axios 响应拦截器自动用 Refresh Token 调用 `/api/auth/refresh`
  4. 后端验证 Refresh Token（查库 + 检查过期），返回新 Access Token
  5. 前端保存新 Token 后重试被拦截的原请求，用户无感知

  **安全特性**：
  - 令牌分离：Access Token 不含刷新能力，Refresh Token 存服务端数据库
  - 主动注销：删除数据库 Refresh Token 即可立即使会话失效
  - 设备追踪：记录创建时的 User-Agent 和 IP 地址
  - 刷新队列：防止多请求并发时多次刷新（雪崩防护）

  ### ⚠️ 前端开发新规范（2026-04-11 新增）

  **所有页面必须引入 `auth.js`**：

  ```html
  <!-- Vue + Element UI + Axios 之后，Vue 实例初始化之前 -->
  <script src="https://cdn.jsdelivr.net/npm/axios@0.27.2/dist/axios.min.js"></script>
  <script src="js/auth.js"></script>  <!-- ← 必须引入！ -->
  <script>
      Vue.use(ELEMENT);
      new Vue({ el: '#app', ... })
  </script>
  ```

  **`auth.js` 提供以下全局函数**（`window.` 导出）：
  - `saveAuth(token, refreshToken, user)` — 登录后保存认证信息
  - `clearAuth()` — 注销时清除认证信息
  - `getRefreshToken()` — 获取 Refresh Token
  - `getStoredUser()` — 获取 localStorage 中的用户信息
  - `writeJwtCookie(token)` — 写入 PET_JWT Cookie（供 Spring Security 页面认证）

  ### ⚠️ user-management.html 角色统计修复（2026-04-11 新增）

  **问题**：统计卡片的 Vue 绑定写错了 key 名，导致宠物医院计数永远为 0。

  - `roleCount` 对象定义：`pet_hospital: 0`（带下划线）
  - 模板引用：`{{ roleCount.hospital }}`（少了下划线）→ `undefined` → 显示 0

  **修复**：将模板中的 `roleCount.hospital` 改为 `roleCount.pet_hospital`。

  数据库中实际有 4 个宠物医院用户（`pet_hospital` 角色）。

  ## 🎯 更新完成（v1.1.4）

  ---

  ## 📌 v1.1.5 更新（2026-04-11）P2 收尾完善 — 排版层级统一 + 移动端适配

  ### 背景

  2026-04-11 下午完成，执行 P2 收尾完善两个子任务。

  ### P2-1：文字排版层级统一

  **修改文件**：`simple-stats.html`、`stats.html`、`adoption-list.html`

  - 主标题：**Nunito 800 / 26px / line-height 1.2**
  - 副标题：**Nunito 400 / 14px / line-height 1.6**
  - 统计数字：**Nunito 800 / 34px / line-height 1**
  - 标签说明：**Nunito 600 / 12px / letter-spacing 0.02em**
  - 卡片标题：**Nunito 700 / 17px / line-height 1.4**
  - `simple-stats.html` 背景升级为 `linear-gradient(160deg, var(--warm-white) 0%, var(--warm-cream) 60%, #f5e8df 100%)`

  ### P2-2：移动端响应式适配（共 9 个页面）

  | 页面 | 响应式改动 |
  |------|-----------|
  | `simple-stats.html` | 900px/600px/375px 三级，含表格滚动/触摸友好 |
  | `adoption-list.html` | 横幅精简/卡片单列/抽屉全屏/触摸按钮 |
  | `volunteer-task.html` | 900px 断点 + 弹窗全屏适配 |
  | `stats.html` | 600px/480px/375px 多级响应式 |
  | `help.html` | 900px/600px + 流程步骤移动端纵向布局 |
  | `health-record.html` | 增强 600px/375px 断点 |
  | `user-management.html` | 900px/600px/375px 三级响应式 |
  | `profile.html` | 增强 + 快速入口网格适配 |
  | `shelter-recommendation.html` | 全面重写：救助站网格/评分区域/按钮组全屏 |

  **统一响应式规范**：
  - 900px：横幅纵向 + 双列卡片
  - 600px：横幅极简 + 统计双列 + 表单全宽 + 触摸按钮 36-38px
  - 375px：单列布局
  - `@media (hover: none)` 禁用无谓 hover
  - `@media (prefers-reduced-motion)` 尊重系统动画偏好

  ### 前端开发新规范补充

  **P2-2 响应式开发规范（2026-04-11 新增）**：

  所有新页面必须包含完整响应式断点：
  ```css
  /* 平板：横幅精简 + 双列统计 */
  @media (max-width: 900px) {
      .hero-banner { flex-direction: column; }
      .stats-grid { grid-template-columns: repeat(2, 1fr); }
  }
  /* 移动端：单列卡片 + 触摸友好 */
  @media (max-width: 600px) {
      .page-content { padding: 12px 12px 32px; }
      .hero-banner { padding: 14px 16px; }
      .stats-grid { grid-template-columns: 1fr 1fr; }
      .el-button, button { min-height: 36px; padding: 8px 14px; }
  }
  /* 超小屏 */
  @media (max-width: 375px) {
      .stats-grid { grid-template-columns: 1fr; }
  }
  @media (hover: none) {
      .card:hover { transform: none !important; box-shadow: var(--card-shadow); }
  }
  ```

  ## 🎯 更新完成（v1.1.5）

---

## 📌 v1.1.6 更新（2026-04-12）宠物数据大规模冲突修复

### 背景

2026-04-12 发现数据库中大量宠物的 `description`、`health_status`、`personality` 字段与品种严重不符。
这是数据录入/OCR 识别错误在批量导入时造成的大规模数据混乱——某些宠物记录被错误地写入了另一只宠物的描述。

### 问题分析

**根本原因**：数据录入时按字段逐行填入，但顺序混乱：
- `笨笨`（id=15）品种为哈士奇，但 description 里写的是"玳瑁猫花花"的内容
- `黑猫警长`（id=4）品种为折耳猫，但 description 写的是"灰色混血犬阿灰"
- `阿福`（id=17）品种为柴犬，但 description 写的是"黑猫黑豆"

**影响范围**：14只宠物 `description/health_status/personality` 字段与品种冲突，1条健康档案 `record_type` 错误。

### 已修复的宠物（14只）

| id | 名字 | 品种 | 修复内容 |
|----|------|------|---------|
| 4 | 黑猫警长 | 英国折耳猫 | description/health_status/personality 修正为折耳猫描述 |
| 8 | 豆豆 | 泰迪犬 | description/health_status/personality 修正为泰迪犬描述 |
| 10 | 奶茶 | 波斯猫/暹罗 | description/personality 修正为暹罗猫描述 |
| 12 | 小灰 | 美国短毛猫 | description 修正为美短猫描述（原本误写了萨摩耶） |
| 14 | 贝塔 | 萨摩耶 | description 修正为比熊犬描述 |
| 15 | 笨笨 | 哈士奇 | description/health_status/personality 修正为哈士奇正确描述 |
| 17 | 阿福 | 柴犬 | description 修正为柴犬描述（原本误写了黑猫） |
| 18 | 多多 | 金毛 | description 修正为金毛犬描述（原本误写了美短猫） |
| 27 | 毛茸茸 | 仓鼠 | description/personality 修正为仓鼠描述（原本误写了兔子） |
| 28 | 小白兔 | 仓鼠 | description 修正为家兔描述（品种和描述颠倒） |
| 29 | 灰灰 | 荷兰猪 | description 修正为荷兰猪描述（原本误写了安哥拉兔） |
| 30 | 小瓜子 | 天竺鼠 | description 修正为仓鼠描述（原本误写了兔子） |
| 31 | 小团子 | 天竺鼠 | description 修正为仓鼠描述（原本误写了仓鼠，重复） |
| 33 | 小年 | 萨摩耶 | description/personality 修正为萨摩耶正确描述 |

### 修复脚本

- `sql/_fix_pet_conflicts.py` — 本次修复脚本
- `sql/_verify_fixes.py` — 验证脚本
- `sql/_query_pets4.py` / `_query_pets3.py` — 数据导出工具
- `sql/_pets_output.json` — 原始数据快照

### 数据完整性说明

- `pet.id`、`pet.breed`、`pet.category_type` 均正确，无需修改
- 所有健康档案 `health_record.pet_id` 外键正确
- 所有领养申请 `adoption_application.pet_id` 外键正确
- 修复仅影响文字描述字段，不影响关联关系

## 🎯 更新完成（v1.1.6）

---

## 📌 v1.2.1 更新（2026-04-12）宠物收藏功能 + GitHub v1.2 推送 + 文档全面更新

### 一、宠物收藏功能开发（v1.2.0 新增）

**数据库**：`pet_favorite` 表（user_id + pet_id 唯一约束）

**后端（6个文件）**：
- `PetFavorite.java` Entity
- `PetFavoriteMapper.java`
- `PetFavoriteService.java` + `PetFavoriteServiceImpl.java`
- `PetFavoriteController.java` — 3个API端点（列表/收藏/取消/检查）
- `pet-favorite.sql` 建表脚本

**前端**：
- `pet-detail.html` — 收藏按钮（爱心图标），点击切换收藏状态
- `profile.html` — 新增「我的收藏」Tab，展示收藏宠物列表

**SecurityConfig**：放行 `/api/favorite/**`（需认证后访问）

### 二、GitHub v1.2 推送

**提交内容**：69个文件，+17164行，-4550行

新增功能：
- 宠物收藏、回访记录、JWT双Token刷新机制
- 前端全面优化（温馨治愈风UI + 统一遮罩层 + 响应式适配）
- 新增页面（帮助中心、宠物医院、简化统计页、成长中心、我的收藏）
- 文档更新

**敏感信息处理**：
- `application.yml`：数据库密码和JWT密钥改为 `${DB_PASSWORD:CHANGE_ME}` 和 `${JWT_SECRET:CHANGE_ME}`
- 以下文件**未提交**：根目录 `application.yml`（含真实密码）、临时调试脚本、内部开发备忘文档

**GitHub**：`https://github.com/runninggoo/pet-rescue-system`

### 三、截图管理文档 v1.2.0 更新

- 截图总数：39张 → **44张**
- 新增模块：志愿者成长体系（5张）、宠物收藏（2张）、宠物医院（1张）、帮助中心（1张）、简化统计页（1张）
- 页面数：14个 → **15个**
- 底部版本记录：v1.2.0

### 四、论文素材保存文档 v1.3 更新

新增内容：
- **创新点**：第5点（志愿者成长激励）、第6点（个性化收藏）
- **核心技术栈**：补充 Element UI 2.15.13、Vite、双Token架构
- **测试数据**：补充 v1.1.6 宠物数据修复说明
- **数据库设计**：补充 pet_category、refresh_token、pet_favorite 表
- **关键代码**：JWT双Token认证机制、志愿者成长等级体系代码
- **前端页面清单**：9个 → **15个页面**
- **答辩Q&A**：新增Q6（双Token优势）、Q7（成长体系设计）、Q8（收藏功能实现）

---

### 原 health-record.html DOM 结构修复（v1.2.1 前置修复）

**问题**：行 846-848 多余的 `</div>` 闭合标签，破坏 Vue 模板 DOM 层级，`{{...}}` 直接显示。

**修复**：删除多余的 `</span>` 和两个 `</div>`，恢复正确的 DOM 结构。

**预防经验**：
1. `</div><!-- end #app -->` 提前闭合
2. DOM 标签未匹配闭合（本例多余 `</div>`）
3. HTML 结构被截断或嵌套错误

## 🎯 更新完成（v1.2.1）