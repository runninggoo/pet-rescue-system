# 宠物救助收养系统 - 项目规范

> 本文件为项目核心规范，新对话必须先阅读此文档。
> 当前版本：v1.2.4 | 最后更新：2026-04-27

---

## 项目概述

宠物救助收养系统是基于 Spring Boot + Vue.js 的前后端分离Web应用，面向上海地区流浪宠物救助收养场景。系统覆盖从"救助发现 → 领养审核 → 健康管理 → 后续跟踪"的全业务流程，支持5类用户角色协同工作。

### 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 2.7.0 + Spring Security + MyBatis Plus 3.5.3.1 + MySQL 8.0 |
| 前端 | Vue.js 2.6.14 + Element UI 2.15.13 + Vite + Axios 0.27.2 |
| 认证 | JWT + Refresh Token 双Token架构 |

### 文件结构

```
backend/src/main/java/com/pet/rescue/
  config/   controller/  entity/  mapper/  service/  security/  utils/  vo/

backend/src/main/resources/
  application.yml    mapper/    static/（前端HTML）

frontend/public/   ← 前端HTML文件（Vite开发服务器端口5173）
```

---

## 前端开发规范（必须遵守）

### 1. CDN 链接必须带版本号

```html
<!-- 正确 -->
<script src="https://unpkg.com/element-ui@2.15.13/lib/index.js"></script>
<link rel="stylesheet" href="https://unpkg.com/element-ui@2.15.13/lib/theme-chalk/index.css">

<!-- 错误：会加载不兼容版本 -->
<script src="https://unpkg.com/element-ui/lib/index.js"></script>
```

### 2. Element UI 必须全局注册

```html
<script src="https://cdn.jsdelivr.net/npm/vue@2.6.14/dist/vue.js"></script>
<script src="https://unpkg.com/element-ui@2.15.13/lib/index.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios@0.27.2/dist/axios.min.js"></script>
<script>
    Vue.use(ELEMENT);   // ← 必须！
</script>
```

### 3. 禁止使用 axios.defaults.baseURL

所有 API 调用必须使用完整相对路径：

```javascript
// 正确
axios.get('/api/pet/list', { params: { page: 1, pageSize: 100 } })
axios.post('/api/auth/login', { phone: '13800000000', password: '123456' })

// 错误：baseURL 在 Axios 1.x 中有bug
axios.defaults.baseURL = '/api';
```

### 4. Vue 组件必须放在 #app 内

所有对话框必须放在 `<div id="app">` 内部：

```html
<body>
  <div id="app">
    <el-dialog>...</el-dialog>   ← 必须在 #app 内
  </div><!-- end #app -->
  <script>new Vue({ el: '#app' })</script>
</body>
```

### 5. el-dialog 必须替换为原生 div 遮罩层

**重要**：`el-dialog` 会挂载到 `document.body`，创建独立的 Vue 实例，导致 Dialog 内所有 `@click` 回调无法访问根 `#app` 实例的方法。

**解决方案**：使用原生 div 覆盖层：

```html
<div v-if="showDialog" class="cat-overlay" @click.self="showDialog = false">
  <div class="cat-panel">
    <div class="cat-panel-header">
      <h3>标题</h3>
      <span @click="showDialog = false">✕</span>
    </div>
    <div class="cat-panel-body">
      <el-button @click="handleSubmit">提交</el-button>
    </div>
  </div>
</div>
```

**CSS 样式参考**：
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

**诊断方法**：弹窗内按钮无响应时，按 F12 在 Console 执行：
```javascript
let el = document.querySelector('.el-button'); // 弹窗内按钮
while (el = el.parentElement) { if (el.__vue__) console.log(Object.keys(el.__vue__).length); }
// ~18个属性 → el-dialog隔离实例（bug）；~79个属性 → 根#app实例（正常）
```

---

## MyBatis Plus 规范（必须遵守）

### 1. 只用 @TableLogic 注解

禁止在 `application.yml` 配置全局逻辑删除，会与 `@TableLogic` 注解冲突导致分页查询 COUNT=0。

### 2. isNull / isNotNull 极易写反

- `parent_id IS NULL` → 根节点（一级分类，如"猫"、"狗"）
- `parent_id IS NOT NULL` → 子节点（品种，如"中华田园猫"）

### 3. 分页 total=0 的快速排查

1. 检查数据库：`SELECT deleted FROM pet WHERE ... LIMIT 1` — 是否有 NULL 值
2. 检查 application.yml：是否有全局逻辑删除配置
3. 如遇 NULL：`UPDATE pet SET deleted=0 WHERE deleted IS NULL`

---

## CSS 设计规范

### 色彩体系

```css
:root {
    --primary: #E07A5F;       /* 陶土珊瑚 — 主色调 */
    --primary-dark: #c96a50;   --primary-light: #f0a28a;
    --primary-bg: rgba(224, 122, 95, 0.08);
    --sage: #81B29A;          --sage-light: #a8cdb8;
    --amber: #F4A261;
    --warm-white: #FDF6F0;   --warm-cream: #FAEEE7;
    --warm-gray: #f0ebe7;     --warm-gray2: #ede6df;
    --text-dark: #3D405B;      --text-muted: #8A8EA0;
    --bg: var(--warm-white);
    --card-shadow: 0 4px 20px rgba(224, 122, 95, 0.10);
    --card-shadow-hover: 0 12px 40px rgba(224, 122, 95, 0.20);
    --transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
    --radius: 20px;   --radius-sm: 12px;
}
```

### 字体

```html
<link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet">
```
```css
body { font-family: "Nunito", "PingFang SC", "Microsoft YaHei", sans-serif; }
```

### Element UI 覆盖样式

```css
.el-button--primary {
    background-color: var(--primary) !important; border-color: var(--primary) !important;
    border-radius: var(--radius-sm) !important;
}
.el-button--primary:hover {
    background-color: var(--primary-dark) !important; transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(224, 122, 95, 0.3);
}
.el-button--success { background-color: var(--sage) !important; border-color: var(--sage) !important; border-radius: var(--radius-sm) !important; }
.el-input__inner:focus { border-color: var(--primary) !important; box-shadow: 0 0 0 3px rgba(224,122,95,0.12) !important; }
.el-pager li.active { background-color: var(--primary) !important; color: white !important; border-radius: 50% !important; }
```

---

## 启动步骤

### 1. 启动数据库
确保 MySQL 运行，数据库 `pet_rescue` 存在，密码 `123456`。

### 2. 启动后端
```bash
cd backend
mvn spring-boot:run
# 或 IDE 运行 PetRescueSystemApplication.java
```
端口 8081，context-path `/api`。

### 3. 启动前端
```bash
cd frontend
npm install   # 首次
npm run dev
```
访问 `http://localhost:5173/` → 自动跳转登录页。

### 4. 同步前端文件
每次修改 `frontend/public/*.html` 后同步到后端：
```powershell
$src = "C:\Users\33169\Desktop\计算机\毕业设计\项目\frontend\public"
$dst = "C:\Users\33169\Desktop\计算机\毕业设计\项目\backend\src\main\resources\static"
Get-ChildItem "$src\*.html" | ForEach-Object { Copy-Item "$src\$($_.Name)" "$dst\$($_.Name)" -Force }
```

---

## 当前版本状态

**版本**：v1.2.4（2026-04-27）
**功能完成度**：100%（16个页面全部可用）
**代码稳定度**：100%

所有历史开发记录、版本更新、Bug修复详情请见 `项目状态保存文档.md`。
