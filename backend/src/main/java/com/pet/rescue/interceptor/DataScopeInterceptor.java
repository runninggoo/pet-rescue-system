package com.pet.rescue.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.pet.rescue.annotation.DataScope;
import com.pet.rescue.security.UserContext;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;

/**
 * 数据范围控制拦截器
 * 用于在SQL执行前根据用户权限动态添加数据过滤条件
 * 
 * @author Claude
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class })
})
// @Component // 临时禁用以诊断问题
public class DataScopeInterceptor implements Interceptor {

    private static final String INSTITUTION_ID_COLUMN = "institution_id";
    private static final String USER_ID_COLUMN = "user_id";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        // 使用反射获取 mappedStatement
        java.lang.reflect.Field field = statementHandler.getClass().getDeclaredField("mappedStatement");
        field.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) field.get(statementHandler);

        // 获取方法上的DataScope注解
        DataScope dataScope = getDataScopeAnnotation(mappedStatement);
        if (dataScope == null) {
            return invocation.proceed(); // 没有注解，直接执行
        }

        // 获取当前用户信息
        Long currentUserId = UserContext.getCurrentUserId();
        String currentUserRole = UserContext.getCurrentUserRole();
        Long currentInstitutionId = UserContext.getCurrentUserInstitutionId();

        // 根据角色和数据范围类型修改SQL
        String modifiedSql = modifySql(boundSql.getSql(), dataScope, currentUserId, currentUserRole,
                currentInstitutionId);

        // 更新BoundSql
        java.lang.reflect.Field sqlField = boundSql.getClass().getDeclaredField("sql");
        sqlField.setAccessible(true);
        sqlField.set(boundSql, modifiedSql);

        return invocation.proceed();
    }

    /**
     * 获取方法上的DataScope注解
     */
    private DataScope getDataScopeAnnotation(MappedStatement mappedStatement) {
        try {
            // 获取Mapper接口类名
            String className = mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf("."));
            // 获取方法名
            String methodName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1);

            Class<?> mapperClass = Class.forName(className);
            // 获取所有方法，找到对应的方法
            Method[] methods = mapperClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return method.getAnnotation(DataScope.class);
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return null;
    }

    /**
     * 修改SQL，添加数据范围过滤条件
     * 简化实现：直接拼接SQL条件
     */
    private String modifySql(String originalSql, DataScope dataScope, Long currentUserId, String currentUserRole,
            Long currentInstitutionId) {
        // 管理员可以访问所有数据
        if (currentUserRole != null && "admin".equals(currentUserRole)) {
            return originalSql;
        }

        StringBuilder modifiedSql = new StringBuilder();
        String upperSql = originalSql.toUpperCase();

        // 判断SQL是否已有WHERE子句
        boolean hasWhere = upperSql.contains(" WHERE ");

        // 构建WHERE条件
        StringBuilder whereClause = new StringBuilder();

        switch (dataScope.type()) {
            case INSTITUTION:
                if (currentInstitutionId != null) {
                    whereClause.append(INSTITUTION_ID_COLUMN).append(" = ").append(currentInstitutionId);
                }
                break;
            case SELF:
                if (currentUserId != null) {
                    whereClause.append(USER_ID_COLUMN).append(" = ").append(currentUserId);
                }
                break;
            case ALL:
            default:
                // 不需要过滤
                return originalSql;
        }

        // 如果没有WHERE条件，直接返回原SQL
        if (whereClause.length() == 0) {
            return originalSql;
        }

        // 添加WHERE条件
        if (hasWhere) {
            // 已有WHERE，添加AND条件
            modifiedSql.append(originalSql)
                    .append(" AND (")
                    .append(whereClause)
                    .append(")");
        } else {
            // 没有WHERE，添加WHERE条件
            int insertIndex = originalSql.toUpperCase().indexOf(" ORDER BY ");
            if (insertIndex == -1) {
                insertIndex = originalSql.length();
            }
            modifiedSql.append(originalSql.substring(0, insertIndex))
                    .append(" WHERE ")
                    .append(whereClause)
                    .append(originalSql.substring(insertIndex));
        }

        return modifiedSql.toString();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        // 不需要特殊配置
    }
}