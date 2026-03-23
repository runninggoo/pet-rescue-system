package com.pet.rescue.service.stats;

import java.util.Map;

public interface StatsService {

    /**
     * 获取救助数量统计
     * @param params 包含startDate, endDate等参数
     * @return 统计结果
     */
    Map<String, Object> getRescueCount(Map<String, Object> params);

    /**
     * 获取领养成功率统计
     * @param params 查询参数
     * @return 统计结果
     */
    Map<String, Object> getAdoptionRate(Map<String, Object> params);

    /**
     * 获取宠物状态分布统计
     * @return 统计结果
     */
    Map<String, Object> getPetStatusDistribution();

    /**
     * 获取机构统计信息
     * @param institutionId 机构ID
     * @return 机构统计结果
     */
    Map<String, Object> getInstitutionStats(Long institutionId);

    /**
     * 获取回访完成率统计
     * @param params 查询参数
     * @return 统计结果
     */
    Map<String, Object> getFollowUpRate(Map<String, Object> params);

    /**
     * 导出统计数据
     * @param params 导出参数
     * @return 导出结果
     */
    String exportStats(Map<String, Object> params);
}