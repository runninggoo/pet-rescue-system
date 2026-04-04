  ## 📋 创建claude.md文件

  ```markdown
  # 宠物救助收养系统 - 项目规范

  ## 🎯 项目概述

  宠物救助收养系统是一个基于Spring Boot +
  Vue.js的前后端分离Web应用，旨在解决上海地区流浪宠物救助收养领域的数字化管理问题。系统覆盖从"救助发现→领养审核→健康管理→后续跟踪"的全业务流程，支持多角色在线协同工作。

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

---最后更新：2026-04-03（品种筛选 Bug 完全修复）
版本：1.1.1
状态：品种筛选修复完成，志愿者成长体系完成，P2 JWT Token 刷新待实现，P0 论文截图采集
新增：品种筛选修复（批量 children 填充 + SecurityConfig permitAll + 前端 loadBreedsByCategory）
重要：品种管理弹窗使用原生 div 覆盖层替代 el-dialog（解决 Vue 隔离问题）
MyBatis isNull/isNotNull 极易写反，详见下方注意事项章节
UI 规范：温馨治愈宠物风（陶土珊瑚色系 #E07A5F），详见下方「前端 UI 设计规范」章节

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