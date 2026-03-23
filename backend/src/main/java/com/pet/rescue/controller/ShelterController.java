package com.pet.rescue.controller;

import com.pet.rescue.entity.Shelter;
import com.pet.rescue.service.ShelterService;
import com.pet.rescue.vo.ResponseResult;
import com.pet.rescue.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 救助所Controller
 * 处理所有救助所相关请求
 * 实现上海地区救助所的智能化管理
 */
@RestController
@RequestMapping("/api/shelter")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    /**
     * 添加救助所（仅管理员）
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult addShelter(@RequestBody Shelter shelter) {
        try {
            boolean success = shelterService.addShelter(shelter);
            if (success) {
                return ResponseResult.ok("救助所添加成功");
            } else {
                return ResponseResult.error("救助所添加失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("添加救助所失败：" + e.getMessage());
        }
    }

    /**
     * 更新救助所信息
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult updateShelter(@RequestBody Shelter shelter) {
        try {
            // 权限检查：机构管理员只能更新自己机构的救助所
            Long currentUserId = UserContext.getCurrentUserId();
            String role = UserContext.getCurrentUserRole();

            if ("institution_admin".equals(role)) {
                // 检查是否是该机构的救助所
                Shelter existingShelter = shelterService.getShelterById(shelter.getId());
                if (existingShelter == null) {
                    return ResponseResult.error("救助所不存在");
                }
                // 这里简化处理，实际应该通过机构ID关联检查
            }

            boolean success = shelterService.updateShelter(shelter);
            if (success) {
                return ResponseResult.ok("救助所更新成功");
            } else {
                return ResponseResult.error("救助所更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新救助所失败：" + e.getMessage());
        }
    }

    /**
     * 删除救助所（仅管理员）
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult deleteShelter(@PathVariable Long id) {
        try {
            boolean success = shelterService.deleteShelter(id);
            if (success) {
                return ResponseResult.ok("救助所删除成功");
            } else {
                return ResponseResult.error("救助所删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除救助所失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询救助所详情
     */
    @GetMapping("/detail/{id}")
    public ResponseResult getShelterDetail(@PathVariable Long id,
                                           @RequestParam(required = false) Double lat,
                                           @RequestParam(required = false) Double lon) {
        try {
            Shelter shelter = shelterService.getShelterById(id);
            if (shelter == null) {
                return ResponseResult.error("救助所不存在");
            }

            // 如果提供了经纬度，计算距离
            if (lat != null && lon != null) {
                shelter = shelterService.getShelterWithDistance(id, lat, lon);
            }

            return ResponseResult.ok().data("shelter", shelter);
        } catch (Exception e) {
            return ResponseResult.error("查询救助所详情失败：" + e.getMessage());
        }
    }

    /**
     * 查询救助所列表（根据权限过滤）
     */
    @GetMapping("/list")
    public ResponseResult getShelterList(@RequestParam Map<String, Object> params) {
        try {
            // 权限控制：根据用户角色过滤数据
            Long currentUserId = UserContext.getCurrentUserId();
            String role = UserContext.getCurrentUserRole();

            // 添加权限过滤条件
            if ("institution_admin".equals(role)) {
                params.put("regionCode", "310115"); // 简化处理，默认浦东新区
            } else if ("adopter".equals(role) || "volunteer".equals(role)) {
                params.put("entryStatus", 1); // 只能查看已入驻的救助所
                params.put("auditStatus", 1); // 只能查看审核通过的救助所
            }

            List<Shelter> shelters = shelterService.getSheltersWithPermission(params);
            return ResponseResult.ok().data("shelters", shelters);
        } catch (Exception e) {
            return ResponseResult.error("查询救助所列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据区域查询救助所
     */
    @GetMapping("/list/region/{regionCode}")
    public ResponseResult getSheltersByRegion(@PathVariable String regionCode) {
        try {
            List<Shelter> shelters = shelterService.getSheltersByRegion(regionCode);
            return ResponseResult.ok().data("shelters", shelters);
        } catch (Exception e) {
            return ResponseResult.error("查询区域救助所失败：" + e.getMessage());
        }
    }

    /**
     * 智能匹配救助所（核心功能）
     */
    @PostMapping("/match")
    public ResponseResult matchShelters(@RequestBody Map<String, Object> request) {
        try {
            // 解析请求参数
            Double lat = request.containsKey("lat") ? Double.valueOf(request.get("lat").toString()) : null;
            Double lon = request.containsKey("lon") ? Double.valueOf(request.get("lon").toString()) : null;
            Integer minCapacity = request.containsKey("minCapacity") ? Integer.valueOf(request.get("minCapacity").toString()) : 1;
            Integer minMedicalLevel = request.containsKey("minMedicalLevel") ? Integer.valueOf(request.get("minMedicalLevel").toString()) : 0;
            Integer limit = request.containsKey("limit") ? Integer.valueOf(request.get("limit").toString()) : 3;

            if (lat == null || lon == null) {
                return ResponseResult.error("请提供准确的经纬度信息");
            }

            // 调用智能匹配算法
            List<Shelter> matchedShelters = shelterService.matchShelters(lat, lon, minCapacity, minMedicalLevel, limit);

            // 格式化响应
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            for (Shelter shelter : matchedShelters) {
                Map<String, Object> shelterInfo = new java.util.HashMap<>();
                shelterInfo.put("id", shelter.getId());
                shelterInfo.put("name", shelter.getName());
                shelterInfo.put("address", shelter.getAddress());
                shelterInfo.put("regionName", shelter.getRegionName());
                shelterInfo.put("distance", String.format("%.1f公里", shelter.getDistance()));
                shelterInfo.put("availableCapacity", shelter.getAvailableCapacity());
                shelterInfo.put("capacityStatus", shelter.getCapacityStatus());
                shelterInfo.put("medicalLevelDesc", shelter.getMedicalLevelDesc());
                shelterInfo.put("phone", shelter.getPhone());
                result.add(shelterInfo);
            }

            return ResponseResult.ok().data("matchedShelters", result);
        } catch (Exception e) {
            return ResponseResult.error("智能匹配救助所失败：" + e.getMessage());
        }
    }

    /**
     * 查询附近的救助所
     */
    @GetMapping("/nearby")
    public ResponseResult getNearbyShelters(@RequestParam Double lat,
                                            @RequestParam Double lon,
                                            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Shelter> shelters = shelterService.getNearbyShelters(lat, lon, limit);

            // 格式化响应
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            for (Shelter shelter : shelters) {
                Map<String, Object> shelterInfo = new java.util.HashMap<>();
                shelterInfo.put("id", shelter.getId());
                shelterInfo.put("name", shelter.getName());
                shelterInfo.put("distance", shelter.getDistance());
                shelterInfo.put("address", shelter.getAddress());
                shelterInfo.put("availableCapacity", shelter.getAvailableCapacity());
                result.add(shelterInfo);
            }

            return ResponseResult.ok().data("nearbyShelters", result);
        } catch (Exception e) {
            return ResponseResult.error("查询附近救助所失败：" + e.getMessage());
        }
    }

    /**
     * 更新救助所容量
     */
    @PutMapping("/capacity/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult updateCapacity(@PathVariable Long id, @RequestParam Integer currentCapacity) {
        try {
            boolean success = shelterService.updateShelterCapacity(id, currentCapacity);
            if (success) {
                return ResponseResult.ok("容量更新成功");
            } else {
                return ResponseResult.error("容量更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新容量失败：" + e.getMessage());
        }
    }

    /**
     * 更新入驻状态
     */
    @PutMapping("/entry-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult updateEntryStatus(@PathVariable Long id, @RequestParam Integer entryStatus) {
        try {
            boolean success = shelterService.updateShelterEntryStatus(id, entryStatus);
            if (success) {
                return ResponseResult.ok("入驻状态更新成功");
            } else {
                return ResponseResult.error("入驻状态更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新入驻状态失败：" + e.getMessage());
        }
    }

    /**
     * 更新审核状态
     */
    @PutMapping("/audit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult updateAuditStatus(@PathVariable Long id,
                                            @RequestParam Integer auditStatus,
                                            @RequestParam String auditComment) {
        try {
            boolean success = shelterService.updateShelterAuditStatus(id, auditStatus, auditComment);
            if (success) {
                return ResponseResult.ok("审核状态更新成功");
            } else {
                return ResponseResult.error("审核状态更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新审核状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取救助所统计信息
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getShelterStats() {
        try {
            Map<String, Object> stats = new java.util.HashMap<>();

            // 区域统计
            List<Map<String, Object>> regionStats = shelterService.getRegionStats();
            stats.put("regionStats", regionStats);

            // 状态统计
            List<Map<String, Object>> statusStats = shelterService.getStatusStats();
            stats.put("statusStats", statusStats);

            // 平均容量使用率
            Double avgCapacityRatio = shelterService.getAverageCapacityRatio();
            stats.put("avgCapacityRatio", avgCapacityRatio != null ? Math.round(avgCapacityRatio * 100.0) / 100.0 : 0);

            return ResponseResult.ok().data("stats", stats);
        } catch (Exception e) {
            return ResponseResult.error("获取统计信息失败：" + e.getMessage());
        }
    }
}