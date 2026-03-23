package com.pet.rescue.controller;

import com.pet.rescue.entity.Pet;
import com.pet.rescue.service.PetService;
import com.pet.rescue.vo.ResponseResult;
import com.pet.rescue.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * 获取宠物列表（根据角色过滤数据）
     */
    @GetMapping("/list")
    public ResponseResult list(@RequestParam Map<String, Object> params) {
        try {
            // 简化权限控制：根据当前用户角色添加过滤条件
            Long currentUserId = UserContext.getCurrentUserId();
            String role = UserContext.getCurrentUserRole();

            if ("institution_admin".equals(role)) {
                // 机构管理员只能查看自己机构的宠物
                params.put("institutionId", currentUserId);
            } else if ("adopter".equals(role)) {
                // 领养人只能查看待领养的宠物
                params.put("status", 0);
            }
            // 管理员可以查看所有宠物（不添加过滤条件）

            List<Pet> pets = petService.findPetsByCondition(params);
            return ResponseResult.ok().data("pets", pets);
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
    public ResponseResult add(@RequestBody Pet pet) {
        try {
            // 设置默认状态为待领养
            pet.setStatus(0);
            boolean success = petService.addPet(pet);
            if (success) {
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
    public ResponseResult update(@RequestBody Pet pet) {
        try {
            boolean success = petService.updatePet(pet);
            if (success) {
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
    public ResponseResult delete(@PathVariable Long petId) {
        try {
            boolean success = petService.deletePet(petId);
            if (success) {
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
    public ResponseResult updateStatus(@PathVariable Long petId, @RequestParam Integer status) {
        try {
            boolean success = petService.updatePetStatus(petId, status);
            if (success) {
                return ResponseResult.ok("更新状态成功");
            } else {
                return ResponseResult.error("更新状态失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新状态失败：" + e.getMessage());
        }
    }
}