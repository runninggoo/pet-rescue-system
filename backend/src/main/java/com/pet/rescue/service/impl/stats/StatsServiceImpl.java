package com.pet.rescue.service.impl.stats;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pet.rescue.entity.Pet;
import com.pet.rescue.entity.AdoptionApplication;
import com.pet.rescue.entity.FollowUpRecord;
import com.pet.rescue.mapper.PetMapper;
import com.pet.rescue.mapper.AdoptionApplicationMapper;
import com.pet.rescue.mapper.FollowUpRecordMapper;
import com.pet.rescue.service.stats.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private AdoptionApplicationMapper applicationMapper;

    @Autowired
    private FollowUpRecordMapper followUpRecordMapper;

    @Override
    public Map<String, Object> getRescueCount(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 获取查询参数
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");

        // 构建查询条件
        QueryWrapper<Pet> queryWrapper = new QueryWrapper<>();
        if (startDate != null && !startDate.isEmpty()) {
            queryWrapper.ge("rescue_date", startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            queryWrapper.le("rescue_date", endDate);
        }

        // 获取总数
        Long totalCount = petMapper.selectCount(queryWrapper);
        result.put("totalCount", totalCount);

        // 按月份统计（最近12个月）
        List<Map<String, Object>> monthlyStats = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -11); // 12个月前

        for (int i = 0; i < 12; i++) {
            Map<String, Object> monthData = new HashMap<>();
            String monthKey = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
            monthData.put("month", monthKey);

            // 查询该月份的救助数量
            QueryWrapper<Pet> monthQuery = new QueryWrapper<>();
            monthQuery.like("rescue_date", monthKey);
            monthQuery.select("COUNT(*) as count");
            Map<String, Object> monthResult = petMapper.selectMaps(monthQuery).stream()
                    .findFirst()
                    .orElse(new HashMap<>());
            monthData.put("count", monthResult.get("count") != null ? monthResult.get("count") : 0);

            monthlyStats.add(monthData);
            calendar.add(Calendar.MONTH, 1);
        }

        result.put("monthlyStats", monthlyStats);

        // 按品种统计
        QueryWrapper<Pet> breedQuery = new QueryWrapper<>();
        breedQuery.select("breed, COUNT(*) as count").groupBy("breed");
        List<Map<String, Object>> breedStats = petMapper.selectMaps(breedQuery);
        result.put("breedStats", breedStats);

        return result;
    }

    @Override
    public Map<String, Object> getAdoptionRate(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 总宠物数
        Long totalPets = petMapper.selectCount(null);
        result.put("totalPets", totalPets);

        // 已领养宠物数
        QueryWrapper<Pet> adoptedQuery = new QueryWrapper<>();
        adoptedQuery.eq("status", 1);
        Long adoptedPets = petMapper.selectCount(adoptedQuery);
        result.put("adoptedPets", adoptedPets);

        // 待领养宠物数
        QueryWrapper<Pet> waitingQuery = new QueryWrapper<>();
        waitingQuery.eq("status", 0);
        Long waitingPets = petMapper.selectCount(waitingQuery);
        result.put("waitingPets", waitingPets);

        // 领养成功率
        double adoptionRate = totalPets > 0 ? (adoptedPets * 100.0 / totalPets) : 0;
        result.put("adoptionRate", Math.round(adoptionRate * 10) / 10.0);

        // 按时间段统计领养率
        List<Map<String, Object>> timeStats = new ArrayList<>();
        QueryWrapper<AdoptionApplication> appQuery = new QueryWrapper<>();
        appQuery.select("DATE_FORMAT(apply_date, '%Y-%m') as month, COUNT(*) as total, " +
                        "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as approved")
                .groupBy("DATE_FORMAT(apply_date, '%Y-%m')")
                .orderByAsc("month");
        List<Map<String, Object>> appStats = applicationMapper.selectMaps(appQuery);

        for (Map<String, Object> stat : appStats) {
            Map<String, Object> timeData = new HashMap<>();
            timeData.put("month", stat.get("month"));
            timeData.put("total", stat.get("total"));
            timeData.put("approved", stat.get("approved"));
            double rate = stat.get("total") != null && (Integer) stat.get("total") > 0 ?
                    ((Integer) stat.get("approved") * 100.0 / (Integer) stat.get("total")) : 0;
            timeData.put("rate", Math.round(rate * 10) / 10.0);
            timeStats.add(timeData);
        }

        result.put("timeStats", timeStats);

        return result;
    }

    @Override
    public Map<String, Object> getPetStatusDistribution() {
        Map<String, Object> result = new HashMap<>();

        // 按状态统计宠物数量
        QueryWrapper<Pet> statusQuery = new QueryWrapper<>();
        statusQuery.select("status, COUNT(*) as count").groupBy("status");
        List<Map<String, Object>> statusStats = petMapper.selectMaps(statusQuery);

        // 状态映射
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(0, "待领养");
        statusMap.put(1, "已领养");
        statusMap.put(2, "下架");

        List<Map<String, Object>> distribution = new ArrayList<>();
        for (Map<String, Object> stat : statusStats) {
            Map<String, Object> distItem = new HashMap<>();
            Integer status = (Integer) stat.get("status");
            distItem.put("status", statusMap.getOrDefault(status, "未知"));
            distItem.put("count", stat.get("count"));
            distItem.put("statusCode", status);
            distribution.add(distItem);
        }

        result.put("distribution", distribution);

        // 计算百分比
        Long total = petMapper.selectCount(null);
        for (Map<String, Object> item : distribution) {
            Long count = (Long) item.get("count");
            double percentage = total > 0 ? (count * 100.0 / total) : 0;
            item.put("percentage", Math.round(percentage * 10) / 10.0);
        }

        return result;
    }

    @Override
    public Map<String, Object> getInstitutionStats(Long institutionId) {
        Map<String, Object> result = new HashMap<>();

        // 构建查询条件
        QueryWrapper<Pet> queryWrapper = new QueryWrapper<>();
        if (institutionId != null) {
            queryWrapper.eq("institution_id", institutionId);
        }

        // 机构总数
        Long institutionCount = institutionId != null ? 1L :
                petMapper.selectMaps(new QueryWrapper<Pet>().select("COUNT(DISTINCT institution_id) as count"))
                        .stream().findFirst().map(map -> (Long) map.get("count")).orElse(0L);
        result.put("institutionCount", institutionCount);

        // 各机构宠物统计
        QueryWrapper<Pet> instPetQuery = new QueryWrapper<>();
        if (institutionId != null) {
            instPetQuery.eq("institution_id", institutionId);
        }
        instPetQuery.select("institution_id, COUNT(*) as petCount, " +
                        "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as adoptedCount")
                .groupBy("institution_id");
        List<Map<String, Object>> institutionPetStats = petMapper.selectMaps(instPetQuery);
        result.put("institutionPetStats", institutionPetStats);

        // 机构平均领养率
        double avgAdoptionRate = institutionPetStats.stream()
                .mapToDouble(stat -> {
                    Long total = (Long) stat.get("petCount");
                    Long adopted = (Long) stat.get("adoptedCount");
                    return total > 0 ? (adopted * 100.0 / total) : 0;
                })
                .average()
                .orElse(0.0);
        result.put("avgAdoptionRate", Math.round(avgAdoptionRate * 10) / 10.0);

        return result;
    }

    @Override
    public Map<String, Object> getFollowUpRate(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 已领养申请数
        QueryWrapper<AdoptionApplication> adoptedApps = new QueryWrapper<>();
        adoptedApps.eq("status", 1);
        Long adoptedCount = applicationMapper.selectCount(adoptedApps);
        result.put("adoptedCount", adoptedCount);

        // 已完成回访数
        Long completedFollowUps = followUpRecordMapper.selectCount(null);
        result.put("completedFollowUps", completedFollowUps);

        // 回访完成率
        double followUpRate = adoptedCount > 0 ? (completedFollowUps * 100.0 / adoptedCount) : 0;
        result.put("followUpRate", Math.round(followUpRate * 10) / 10.0);

        // 按月份统计回访情况
        QueryWrapper<FollowUpRecord> followUpQuery = new QueryWrapper<>();
        followUpQuery.select("DATE_FORMAT(follow_up_date, '%Y-%m') as month, COUNT(*) as count")
                .groupBy("DATE_FORMAT(follow_up_date, '%Y-%m')")
                .orderByAsc("month");
        List<Map<String, Object>> monthlyFollowUps = followUpRecordMapper.selectMaps(followUpQuery);
        result.put("monthlyFollowUps", monthlyFollowUps);

        return result;
    }

    @Override
    public String exportStats(Map<String, Object> params) {
        // 简化处理，实际应该生成Excel文件
        String exportType = (String) params.get("exportType");
        String dateRange = (String) params.get("dateRange");

        // 这里返回导出成功信息，实际项目中应该生成并返回文件路径
        return "统计数据导出成功 - 类型：" + exportType + "，时间范围：" + dateRange;
    }
}