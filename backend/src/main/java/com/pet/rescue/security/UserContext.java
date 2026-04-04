package com.pet.rescue.security;

import com.pet.rescue.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户上下文管理
 * 用于获取当前登录用户信息
 */
public class UserContext {

    /**
     * 获取当前登录用户
     * @return 当前用户，未登录返回null
     */
    public static User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                return ((CustomUserDetails) authentication.getPrincipal()).getUser();
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
     * 获取当前用户名
     * @return 用户名（name字段），未登录返回null
     */
    public static String getCurrentUsername() {
        User user = getCurrentUser();
        return user != null ? user.getName() : null;
    }

    /**
     * 获取当前用户机构ID
     * 如果是机构管理员，返回机构ID；否则返回null
     * @return 机构ID
     */
    public static Long getCurrentUserInstitutionId() {
        User user = getCurrentUser();
        if (user != null && "institution_admin".equals(user.getRole())) {
            return user.getId();
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

        if ("admin".equals(user.getRole())) {
            return true;
        }

        if ("institution_admin".equals(user.getRole())) {
            Long userInstitutionId = getCurrentUserInstitutionId();
            return userInstitutionId != null && userInstitutionId.equals(institutionId);
        }

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

        if ("admin".equals(user.getRole())) {
            return true;
        }

        return user.getId().equals(userId);
    }

    /**
     * 清除当前用户上下文
     */
    public static void clear() {
        SecurityContextHolder.clearContext();
    }
}
