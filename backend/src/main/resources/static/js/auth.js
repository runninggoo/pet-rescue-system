/**
 * Axios 拦截器模块 - Token 自动刷新 + Cookie 备份
 * 
 * 功能：
 * 1. 请求拦截：自动在请求头中添加 Authorization
 * 2. Cookie 备份：将 JWT token 写入 cookie，便于 Spring Security 页面级认证
 * 3. 响应拦截：401 时自动使用 RefreshToken 刷新 AccessToken
 * 4. 统一错误处理
 */

// 是否正在刷新的标志（防止多个请求同时刷新）
let isRefreshing = false;
// 刷新失败后的请求队列
let refreshSubscribers = [];

// 订阅请求（刷新失败时排队）
function subscribeTokenRefresh(callback) {
    refreshSubscribers.push(callback);
}

// 通知所有等待的请求
function onRefreshed(newToken) {
    refreshSubscribers.forEach(callback => callback(newToken));
    refreshSubscribers = [];
}

// 获取本地存储的用户信息
function getStoredUser() {
    const userStr = localStorage.getItem('user');
    if (!userStr) return null;
    try {
        return JSON.parse(userStr);
    } catch {
        return null;
    }
}

// 写入 JWT cookie（供 Spring Security 页面级认证使用）
function writeJwtCookie(token) {
    try {
        const expires = new Date();
        expires.setDate(expires.getDate() + 7); // 7天有效期
        document.cookie = 'PET_JWT=' + encodeURIComponent(token)
            + ';expires=' + expires.toUTCString()
            + ';path=/'
            + ';SameSite=Lax';
    } catch(e) {}
}

function clearJwtCookie() {
    try {
        document.cookie = 'PET_JWT=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/';
    } catch(e) {}
}

// 保存认证信息（同时写入 localStorage 和 cookie）
function saveAuth(token, refreshToken, user) {
    localStorage.setItem('token', token);
    if (refreshToken) {
        localStorage.setItem('refreshToken', refreshToken);
    }
    if (user) {
        localStorage.setItem('user', JSON.stringify(user));
    }
    // 写入 cookie 供 Spring Security 页面认证使用
    writeJwtCookie(token);
}

// 清除认证信息
function clearAuth() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    clearJwtCookie();
}

// 获取 RefreshToken
function getRefreshToken() {
    return localStorage.getItem('refreshToken');
}

// 请求拦截器
axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        return config;
    },
    error => Promise.reject(error)
);

// 响应拦截器
axios.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;

        // 如果是 401 未授权
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            // 如果是登录/刷新接口本身返回401，说明refreshToken也失效了
            if (originalRequest.url.includes('/auth/login') || 
                originalRequest.url.includes('/auth/refresh')) {
                clearAuth();
                window.location.href = '/login.html';
                return Promise.reject(error);
            }

            // 标记为已重试
            originalRequest._retry = true;

            // 如果没有刷新令牌，跳转登录
            const refreshToken = getRefreshToken();
            if (!refreshToken) {
                clearAuth();
                window.location.href = '/login.html';
                return Promise.reject(error);
            }

            // 如果正在刷新，将请求加入队列
            if (isRefreshing) {
                return new Promise(resolve => {
                    subscribeTokenRefresh(newToken => {
                        originalRequest.headers['Authorization'] = 'Bearer ' + newToken;
                        resolve(axios(originalRequest));
                    });
                });
            }

            // 开始刷新
            isRefreshing = true;

            try {
                const res = await axios.post('/api/auth/refresh', {
                    refreshToken: refreshToken
                });

                if (res.data.code === 200) {
                    const { token, refreshToken: newRefreshToken } = res.data.data;
                    const user = getStoredUser();
                    
                    // 保存新token
                    saveAuth(token, newRefreshToken, user);

                    // 通知所有等待的请求
                    onRefreshed(token);

                    // 重试原请求
                    originalRequest.headers['Authorization'] = 'Bearer ' + token;
                    return axios(originalRequest);
                } else {
                    // 刷新失败，清除认证信息
                    clearAuth();
                    window.location.href = '/login.html';
                    return Promise.reject(error);
                }
            } catch (refreshError) {
                clearAuth();
                window.location.href = '/login.html';
                return Promise.reject(refreshError);
            } finally {
                isRefreshing = false;
            }
        }

        return Promise.reject(error);
    }
);

// 导出工具函数（供登录/注册页面调用）
window.saveAuth = saveAuth;
window.clearAuth = clearAuth;
window.getRefreshToken = getRefreshToken;
window.getStoredUser = getStoredUser;
window.writeJwtCookie = writeJwtCookie;
window.clearJwtCookie = clearJwtCookie;
