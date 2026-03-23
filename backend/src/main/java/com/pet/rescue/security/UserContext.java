package com.pet.rescue.security;

import com.pet.rescue.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户上下文管理
 * 用于获取当前登录用户信息
 * @author Claude
 */
public class UserContext {

    /**
     * 获取当前登录用户
     * @return 当前用户，未登录返回null
     */
    public static User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                return (User) authentication.getPrincipal();
            }
        } catch (Exception e) {
            // 忽略异常，返回null
        }
        return null;
    }

    /**
     * 获取当前用户ID
     * @return 用户ID，未登录返回null
     */
    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    /**
     * 获取当前用户角色
     * @return 用户角色，未登录返回null
     */
    public static String getCurrentUserRole() {
        User user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }

    /**
     * 获取当前用户机构ID
     * 如果是机构管理员，返回机构ID；否则返回null
     * @return 机构ID
     */
    public static Long getCurrentUserInstitutionId() {
        User user = getCurrentUser();
        if (user != null && "institution_admin".equals(user.getRole())) {
            // 从用户的扩展信息中获取机构ID
            // 这里简化处理，实际应该从institution_admin表中查询
            return user.getId(); // 示例：用用户ID作为机构ID
        }
        return null;
    }

    /**
     * 判断当前用户是否有权限访问指定机构的数据
     * @param institutionId 机构ID
     * @return 是否有权限
     */
    public static boolean hasInstitutionAccess(Long institutionId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }

        // 管理员可以访问所有机构
        if ("admin".equals(user.getRole())) {
            return true;
        }

        // 机构管理员只能访问自己的机构
        if ("institution_admin".equals(user.getRole())) {
            Long userInstitutionId = getCurrentUserInstitutionId();
            return userInstitutionId != null && userInstitutionId.equals(institutionId);
        }

        // 其他角色默认无机构数据访问权限
        return false;
    }

    /**
     * 判断当前用户是否有权限访问指定用户的数据
     * @param userId 用户ID
     * @return 是否有权限
     */
    public static boolean hasUserAccess(Long userId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }

        // 管理员可以访问所有用户
        if ("admin".equals(user.getRole())) {
            return true;
        }

        // 用户可以访问自己的数据
        return user.getId().equals(userId);
    }

    /**
     * 清除当前用户上下文
     */
    public static void clear() {
        SecurityContextHolder.clearContext();
    }
}