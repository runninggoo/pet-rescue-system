package com.pet.rescue.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pet.rescue.entity.Pet;
import com.pet.rescue.service.OperationLogService;
import com.pet.rescue.service.PetService;
import com.pet.rescue.vo.ResponseResult;
import com.pet.rescue.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;
    private final OperationLogService operationLogService;

    public PetController(PetService petService, OperationLogService operationLogService) {
        this.petService = petService;
        this.operationLogService = operationLogService;
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取宠物列表（根据角色过滤数据）
     */
    @GetMapping("/list")
    public ResponseResult list(@RequestParam Map<String, Object> params) {
        try {
            // 创建参数副本，避免修改原始请求参数
            Map<String, Object> queryParams = new HashMap<>(params);

            // 简化权限控制：根据当前用户角色添加过滤条件
            Long currentUserId = UserContext.getCurrentUserId();
            String role = UserContext.getCurrentUserRole();

            if ("institution_admin".equals(role)) {
                // 机构管理员只能查看自己机构的宠物
                queryParams.put("institutionId", currentUserId);
            } else if ("adopter".equals(role)) {
                // 领养人只能查看待领养的宠物
                queryParams.put("status", 0);
            }
            // 管理员可以查看所有宠物（不添加过滤条件）

            IPage<Pet> pageResult = petService.findPetsByCondition(queryParams);
            Map<String, Object> data = new HashMap<>();
            data.put("pets", pageResult.getRecords());
            data.put("total", pageResult.getTotal());
            data.put("page", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());
            data.put("pages", pageResult.getPages());
            return ResponseResult.ok().data(data);
        } catch (Exception e) {
            return ResponseResult.error("获取宠物列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取宠物详情（公共接口）
     */
    @GetMapping("/detail/{petId}")
    public ResponseResult detail(@PathVariable Long petId) {
        try {
            Pet pet = petService.findPetDetail(petId);
            if (pet != null) {
                return ResponseResult.ok().data("pet", pet);
            } else {
                return ResponseResult.error("宠物不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("获取宠物详情失败：" + e.getMessage());
        }
    }

    /**
     * 添加宠物（仅管理员）
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult add(@RequestBody Pet pet, HttpServletRequest request) {
        try {
            // 设置默认状态为待领养
            pet.setStatus(0);
            boolean success = petService.addPet(pet);
            if (success) {
                // 记录日志
                operationLogService.logOperation(
                    UserContext.getCurrentUserId(),
                    UserContext.getCurrentUsername(),
                    "PET_ADD",
                    "添加宠物：「" + pet.getName() + "」，品种：「" + pet.getBreed() + "」",
                    getIp(request),
                    UserContext.getCurrentUserRole()
                );
                return ResponseResult.ok("添加宠物成功");
            } else {
                return ResponseResult.error("添加宠物失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("添加宠物失败：" + e.getMessage());
        }
    }

    /**
     * 更新宠物信息（仅管理员）
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult update(@RequestBody Pet pet, HttpServletRequest request) {
        try {
            boolean success = petService.updatePet(pet);
            if (success) {
                // 记录日志
                operationLogService.logOperation(
                    UserContext.getCurrentUserId(),
                    UserContext.getCurrentUsername(),
                    "PET_UPDATE",
                    "更新宠物信息：ID=" + pet.getId() + "，「" + pet.getName() + "」",
                    getIp(request),
                    UserContext.getCurrentUserRole()
                );
                return ResponseResult.ok("更新宠物成功");
            } else {
                return ResponseResult.error("更新宠物失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新宠物失败：" + e.getMessage());
        }
    }

    /**
     * 删除宠物（仅管理员）
     */
    @DeleteMapping("/delete/{petId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult delete(@PathVariable Long petId, HttpServletRequest request) {
        try {
            Pet pet = petService.findPetDetail(petId);
            String petName = pet != null ? pet.getName() : "ID=" + petId;
            boolean success = petService.deletePet(petId);
            if (success) {
                // 记录日志
                operationLogService.logOperation(
                    UserContext.getCurrentUserId(),
                    UserContext.getCurrentUsername(),
                    "PET_DELETE",
                    "删除宠物：ID=" + petId + "，「" + petName + "」",
                    getIp(request),
                    UserContext.getCurrentUserRole()
                );
                return ResponseResult.ok("删除宠物成功");
            } else {
                return ResponseResult.error("删除宠物失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除宠物失败：" + e.getMessage());
        }
    }

    /**
     * 更新宠物状态（仅管理员）
     */
    @PutMapping("/status/{petId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult updateStatus(@PathVariable Long petId, @RequestParam Integer status, HttpServletRequest request) {
        try {
            Pet pet = petService.findPetDetail(petId);
            boolean success = petService.updatePetStatus(petId, status);
            if (success) {
                String statusName = status == 0 ? "待领养" : (status == 1 ? "已领养" : "下架");
                operationLogService.logOperation(
                    UserContext.getCurrentUserId(),
                    UserContext.getCurrentUsername(),
                    "PET_STATUS",
                    "更新宠物状态：ID=" + petId + " → 「" + statusName + "」",
                    getIp(request),
                    UserContext.getCurrentUserRole()
                );
                return ResponseResult.ok("更新状态成功");
            } else {
                return ResponseResult.error("更新状态失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新状态失败：" + e.getMessage());
        }
    }
}