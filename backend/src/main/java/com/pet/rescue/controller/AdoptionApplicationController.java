package com.pet.rescue.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
            // 自动填充申请人ID（从安全上下文获取）
            Long userId = UserContext.getCurrentUserId();
            application.setApplicantId(userId);

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
            Long userId = getCurrentUserId();
            List<AdoptionApplication> applications = applicationService.findByApplicantId(userId);
            return ResponseResult.ok().data("applications", applications);
        } catch (Exception e) {
            return ResponseResult.error("获取申请列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取申请列表（无status参数，返回所有状态）
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getAllApplications(@RequestParam Map<String, Object> params) {
        try {
            Long currentUserId = getCurrentUserId();
            String role = getCurrentUserRole();
            if ("institution_admin".equals(role)) {
                params.put("institutionId", currentUserId);
            }
            IPage<AdoptionApplication> pageResult = applicationService.findByCondition(params);
            Map<String, Object> data = new HashMap<>();
            data.put("applications", pageResult.getRecords());
            data.put("total", pageResult.getTotal());
            data.put("page", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());
            data.put("pages", pageResult.getPages());
            return ResponseResult.ok().data(data);
        } catch (Exception e) {
            return ResponseResult.error("获取申请列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据状态查询申请列表（根据角色过滤）
     */
    @GetMapping("/list/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getApplicationsByStatus(@PathVariable Integer status,
                                                  @RequestParam Map<String, Object> params) {
        try {
            Long currentUserId = getCurrentUserId();
            String role = getCurrentUserRole();

            params.put("status", status);

            if ("institution_admin".equals(role)) {
                params.put("institutionId", currentUserId);
            }

            IPage<AdoptionApplication> pageResult = applicationService.findByCondition(params);
            Map<String, Object> data = new HashMap<>();
            data.put("applications", pageResult.getRecords());
            data.put("total", pageResult.getTotal());
            data.put("page", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());
            data.put("pages", pageResult.getPages());
            return ResponseResult.ok().data(data);
        } catch (Exception e) {
            return ResponseResult.error("获取申请列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取单个申请详情
     */
    @GetMapping("/detail/{applicationId}")
    @PreAuthorize("hasRole('ADOPTER') or hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getApplicationDetail(@PathVariable Long applicationId) {
        try {
            AdoptionApplication application = applicationService.findById(applicationId);
            if (application == null) {
                return ResponseResult.error("申请记录不存在");
            }

            // 领养人只能查看自己的申请
            Long userId = getCurrentUserId();
            String role = getCurrentUserRole();
            if ("adopter".equals(role) && !application.getApplicantId().equals(userId)) {
                return ResponseResult.error("无权限查看此申请");
            }

            return ResponseResult.ok().data("application", application);
        } catch (Exception e) {
            return ResponseResult.error("获取申请详情失败：" + e.getMessage());
        }
    }

    /**
     * 审核申请（管理员）
     */
    @PutMapping("/review/{applicationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult reviewApplication(@PathVariable Long applicationId,
                                           @RequestParam Integer status,
                                           @RequestParam(required = false) String reviewComment) {
        try {
            boolean success = applicationService.reviewApplication(applicationId, status, reviewComment);
            if (success) {
                String msg = (status == 1) ? "审核通过，请让申请人签署领养协议"
                        : (status == 2) ? "已拒绝申请"
                        : "已标记为待补充";
                return ResponseResult.ok(msg);
            } else {
                return ResponseResult.error("审核失败");
            }
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 签署领养协议（领养人操作，将状态从4转为5）
     */
    @PutMapping("/sign/{applicationId}")
    @PreAuthorize("hasRole('ADOPTER')")
    public ResponseResult signContract(@PathVariable Long applicationId) {
        try {
            // 验证申请是否属于当前用户
            AdoptionApplication application = applicationService.findById(applicationId);
            if (application == null) {
                return ResponseResult.error("申请记录不存在");
            }
            Long userId = getCurrentUserId();
            if (!application.getApplicantId().equals(userId)) {
                return ResponseResult.error("无权限操作此申请");
            }

            boolean success = applicationService.signContract(applicationId);
            if (success) {
                return ResponseResult.ok("领养协议签署完成，领养流程已全部完成！");
            } else {
                return ResponseResult.error("签署失败");
            }
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 获取各状态申请数量统计（用于前端状态标签栏）
     */
    @GetMapping("/stats/counts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getStatusCounts() {
        try {
            Map<Integer, Long> counts = applicationService.getStatusCounts();
            return ResponseResult.ok().data("counts", counts);
        } catch (Exception e) {
            return ResponseResult.error("获取统计数据失败：" + e.getMessage());
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
     * 获取某宠物的所有待审核申请（用于多申请人对比）
     * 仅返回 status=0 的申请
     */
    @GetMapping("/compare/{petId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult compareApplicants(@PathVariable Long petId) {
        try {
            List<AdoptionApplication> applications = applicationService.findPendingByPetId(petId);
            Map<String, Object> data = new HashMap<>();
            data.put("applications", applications);
            data.put("count", applications.size());
            return ResponseResult.ok().data(data);
        } catch (Exception e) {
            return ResponseResult.error("获取对比数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户ID（辅助方法）
     */
    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }

    /**
     * 获取当前用户角色（辅助方法）
     */
    private String getCurrentUserRole() {
        return UserContext.getCurrentUserRole();
    }
}
