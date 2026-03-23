import axios from 'axios';

const API_URL = '/api/stats';

export const statsApi = {
  // 获取救助数量统计
  getRescueCount(params) {
    return axios.get(`${API_URL}/rescue-count`, { params });
  },

  // 获取领养成功率统计
  getAdoptionRate(params) {
    return axios.get(`${API_URL}/adoption-rate`, { params });
  },

  // 获取宠物状态分布
  getPetStatusDistribution() {
    return axios.get(`${API_URL}/pet-status-distribution`);
  },

  // 获取机构统计
  getInstitutionStats(institutionId) {
    return axios.get(`${API_URL}/institution-stats`, {
      params: { institutionId }
    });
  },

  // 获取回访完成率统计
  getFollowUpRate(params) {
    return axios.get(`${API_URL}/follow-up-rate`, { params });
  },

  // 导出统计数据
  exportStats(params) {
    return axios.post(`${API_URL}/export`, params);
  }
};