@echo off
REM 后端启动脚本
REM 使用说明：
REM 1. 直接双击运行，或在项目根目录命令行运行
REM 2. 本脚本会自动定位到 backend 目录并启动 Spring Boot
REM 3. 启动后等待约15秒，通过健康检查确认服务状态

REM 获取脚本所在目录（项目根目录）
set SCRIPT_DIR=%~dp0
REM 切换到后端目录
cd /d "%SCRIPT_DIR%backend"

echo Starting backend...
start /b mvn spring-boot:run > backend.log 2>&1
echo Backend started in background, checking health...
timeout /t 15 /nobreak >nul
curl -s http://localhost:8081/api/health >nul 2>&1 && echo "Backend is running!" || echo "Backend may still be starting..."
