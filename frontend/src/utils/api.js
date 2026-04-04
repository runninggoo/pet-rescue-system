/**
 * 统一API工具模块
 * 包含：JWT认证拦截器、401重定向、统一错误处理
 */

import axios from 'axios';
import { Message } from 'element-ui';

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' }
});

// 请求拦截器：自动附加JWT Token
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// 响应拦截器：统一错误处理
api.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code !== undefined && res.code !== 200) {
      const silentCodes = [400, 401, 403];
      if (!silentCodes.includes(res.code)) {
        Message.error({ message: res.message || '操作失败', duration: 3000 });
      }
      return Promise.reject(new ApiError(res.code, res.message || '业务错误'));
    }
    return response;
  },
  error => {
    if (error.response) {
      const { status, data } = error.response;
      switch (status) {
        case 401:
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          Message.error({ message: '登录已过期，请重新登录', duration: 3000 });
          setTimeout(() => {
            if (window.location.pathname !== '/login.html') {
              window.location.href = 'login.html';
            }
          }, 1500);
          break;
        case 403:
          Message.error({ message: '无权限访问', duration: 3000 });
          break;
        case 404:
          Message.error({ message: '请求的资源不存在', duration: 3000 });
          break;
        case 500:
          Message.error({ message: '服务器内部错误，请稍后重试', duration: 3000 });
          break;
        default:
          Message.error({ message: data?.message || `请求失败 (${status})`, duration: 3000 });
      }
    } else if (error.request) {
      Message.error({ message: '网络连接失败，请检查网络', duration: 3000 });
    } else {
      Message.error({ message: '请求配置错误', duration: 3000 });
    }
    return Promise.reject(error);
  }
);

export class ApiError extends Error {
  constructor(code, message) {
    super(message);
    this.code = code;
    this.name = 'ApiError';
  }
}

// ============ 业务API封装 ============

export const authApi = {
  login: (phone, password) => api.post('/auth/login', { phone, password }),
  register: (data) => api.post('/auth/register', data),
  getCurrentUser: () => api.get('/auth/me')
};

export const petApi = {
  list: (params) => api.get('/pet/list', { params }),
  detail: (id) => api.get(`/pet/detail/${id}`),
  add: (data) => api.post('/pet/add', data),
  update: (data) => api.put('/pet/update', data),
  delete: (id) => api.delete(`/pet/delete/${id}`),
  updateStatus: (id, status) => api.put(`/pet/status/${id}`, null, { params: { status } })
};

export const healthRecordApi = {
  list: (params) => api.get('/health-record/list', { params }),
  byPet: (petId) => api.get(`/health-record/pet/${petId}`),
  detail: (id) => api.get(`/health-record/detail/${id}`),
  add: (data) => api.post('/health-record/add', data),
  update: (data) => api.put('/health-record/update', data),
  delete: (id) => api.delete(`/health-record/delete/${id}`),
  reminders: (days) => api.get('/health-record/reminders', { params: { days } }),
  stats: (petId) => api.get(`/health-record/stats/${petId}`)
};

export const volunteerTaskApi = {
  list: (params) => api.get('/volunteer-task/list', { params }),
  available: () => api.get('/volunteer-task/available'),
  detail: (id) => api.get(`/volunteer-task/detail/${id}`),
  create: (data) => api.post('/volunteer-task/create', data),
  update: (data) => api.put('/volunteer-task/update', data),
  delete: (id) => api.delete(`/volunteer-task/delete/${id}`),
  claim: (id) => api.post(`/volunteer-task/claim/${id}`),
  complete: (id, result) => api.post(`/volunteer-task/complete/${id}`, { result }),
  cancel: (id) => api.post(`/volunteer-task/cancel/${id}`),
  myTasks: () => api.get('/volunteer-task/my-tasks'),
  stats: () => api.get('/volunteer-task/stats')
};

export const adoptionApi = {
  apply: (data) => api.post('/adoption/apply', data),
  myApplications: () => api.get('/adoption/my-applications'),
  list: (params) => api.get('/adoption/list', { params }),
  listByStatus: (status) => api.get(`/adoption/list/${status}`),
  review: (id, status, comment) => api.put(`/adoption/review/${id}`, null, {
    params: { status, reviewComment: comment }
  }),
  check: (petId) => api.get(`/adoption/check/${petId}`)
};

export const shelterApi = {
  list: (params) => api.get('/shelter/list', { params }),
  detail: (id) => api.get(`/shelter/detail/${id}`),
  recommend: (data) => api.post('/shelter-recommendation/recommend', data)
};

export const statsApi = {
  rescueCount: (params) => api.get('/stats/rescue-count', { params }),
  adoptionRate: (params) => api.get('/stats/adoption-rate', { params }),
  petStatusDistribution: () => api.get('/stats/pet-status-distribution'),
  followUpRate: (params) => api.get('/stats/follow-up-rate', { params }),
  institutionStats: (id) => api.get('/stats/institution-stats', { params: { institutionId: id } }),
  export: (params) => api.post('/stats/export', params)
};

export const userApi = {
  list: (params) => api.get('/user/list', { params }),
  detail: (id) => api.get(`/user/detail/${id}`),
  add: (data) => api.post('/user/add', data),
  update: (data) => api.put('/user/update', data),
  delete: (id) => api.delete(`/user/delete/${id}`),
  updateStatus: (id, status) => api.put(`/user/status/${id}`, null, { params: { status } })
};

// ============ 工具函数 ============

export function getCurrentUser() {
  try {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  } catch {
    return null;
  }
}

export function getUserRole() {
  const user = getCurrentUser();
  return user?.role || '';
}

export function isLoggedIn() {
  return !!localStorage.getItem('token');
}

export function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  window.location.href = 'login.html';
}

export function hasRole(...roles) {
  return roles.includes(getUserRole());
}

export function isAdmin() {
  const role = getUserRole();
  return role === 'admin' || role === 'institution_admin';
}

export function isVolunteer() {
  return getUserRole() === 'volunteer';
}

export async function get(url, params = {}, showLoading = false) {
  if (showLoading) Message.info({ message: '加载中...', duration: 0 });
  try {
    const res = await api.get(url, { params });
    if (showLoading) Message.closeAll();
    return res;
  } catch (err) {
    if (showLoading) Message.closeAll();
    throw err;
  }
}

export async function post(url, data = {}) {
  return api.post(url, data);
}

export async function put(url, data = null, config = {}) {
  return api.put(url, data, config);
}

export async function del(url) {
  return api.delete(url);
}

export default api;
