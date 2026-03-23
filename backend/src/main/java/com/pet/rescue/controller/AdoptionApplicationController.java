package com.pet.rescue.controller;

import com.pet.rescue.entity.AdoptionApplication;
import com.pet.rescue.service.AdoptionApplicationService;
import com.pet.rescue.vo.ResponseResult;
import com.pet.rescue.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adoption")
public class AdoptionApplicationController {

    private final AdoptionApplicationService applicationService;

    public AdoptionApplicationController(AdoptionApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * 提交领养申请
     */
    @PostMapping("/apply")
    @PreAuthorize("hasRole('ADOPTER')")
    public ResponseResult submitApplication(@RequestBody AdoptionApplication application) {
        try {
            boolean success = applicationService.submitApplication(application);
            if (success) {
                return ResponseResult.ok("申请提交成功！请等待管理员审核");
            } else {
                return ResponseResult.error("申请提交失败");
            }
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 获取用户的申请列表
     */
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('ADOPTER')")
    public ResponseResult getMyApplications() {
        try {
            // 从安全上下文中获取当前用户ID
            Long userId = getCurrentUserId();
            List<AdoptionApplication> applications = applicationService.findByApplicantId(userId);
            return ResponseResult.ok().data("applications", applications);
        } catch (Exception e) {
            return ResponseResult.error("获取申请列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据状态查询申请列表（根据角色过滤）
     */
    @GetMapping("/list/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getApplicationsByStatus(@PathVariable Integer status) {
        try {
            // 简化权限控制：管理员查看所有，机构管理员查看本机构的申请
            Long currentUserId = getCurrentUserId();
            String role = getCurrentUserRole();

            Map<String, Object> params = new HashMap<>();
            params.put("status", status);

            if ("institution_admin".equals(role)) {
                // 机构管理员只能查看自己机构发布的宠物的申请
                params.put("institutionId", currentUserId);
            }

            List<AdoptionApplication> applications = applicationService.findByCondition(params);
            return ResponseResult.ok().data("applications", applications);
        } catch (Exception e) {
            return ResponseResult.error("获取申请列表失败：" + e.getMessage());
        }
    }

    /**
     * 审核申请（管理员）
     */
    @PutMapping("/review/{applicationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult reviewApplication(@PathVariable Long applicationId,
                                           @RequestParam Integer status,
                                           @RequestParam String reviewComment) {
        try {
            boolean success = applicationService.reviewApplication(applicationId, status, reviewComment);
            if (success) {
                return ResponseResult.ok("审核完成");
            } else {
                return ResponseResult.error("审核失败");
            }
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 检查用户是否已申请某宠物
     */
    @GetMapping("/check/{petId}")
    @PreAuthorize("hasRole('ADOPTER')")
    public ResponseResult checkApplication(@PathVariable Long petId) {
        try {
            Long userId = getCurrentUserId();
            boolean hasApplied = applicationService.hasApplied(petId, userId);
            return ResponseResult.ok().data("hasApplied", hasApplied);
        } catch (Exception e) {
            return ResponseResult.error("检查申请状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户ID（辅助方法）
     */
    private Long getCurrentUserId() {
        // 实际项目中应该从安全上下文中获取
        // 这里简化处理，实际使用时需要完善
        return UserContext.getCurrentUserId(); // 使用UserContext获取用户ID
    }

    /**
     * 获取当前用户角色（辅助方法）
     */
    private String getCurrentUserRole() {
        // 实际项目中应该从安全上下文中获取
        // 这里简化处理，实际使用时需要完善
        return UserContext.getCurrentUserRole(); // 使用UserContext获取用户角色
    }
}