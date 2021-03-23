package com.junyi.permission.interceptor;

import com.junyi.permission.annotation.XzcodeFilter;
import com.junyi.permission.service.UserService;
import com.junyi.permission.util.SpringContextUtil;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author erma66.feng
 * @email erma66@sina.cn
 * @date 2021-02-16
 * @description sql拦截器，用来通过xzcode过滤数据
 */
@Component
@Intercepts({
    @Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(
            type = Executor.class,
            method = "query",
            args = {
                MappedStatement.class,
                Object.class,
                RowBounds.class,
                ResultHandler.class,
                CacheKey.class,
                BoundSql.class
            })
})
public class DataXzcodeFilterInterceptor implements Interceptor {

    @Value("${junyi.data.admin-xzcode:0}")
    private String adminXzcode;

    // 这个Bean不能自动注入，否则会形成循环依赖，需要使用的时候从BeanFactory中获取
    private UserService userService;

    @Resource private HttpServletRequest request;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 接口发起的查询才进行过滤
        if (RequestContextHolder.getRequestAttributes() == null) {
            return invocation.proceed();
        }
        if (userService == null) {
            userService = SpringContextUtil.getBean(UserService.class);
        }
        String xzcode = userService.getXzcode(request);
        if (StringUtils.isEmpty(xzcode)) {
            return invocation.proceed();
        }
        // 数据管理员不过滤数据
        if (xzcode.equals(adminXzcode)) {
            return invocation.proceed();
        }

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        int index = ms.getId().lastIndexOf('.');
        String className = ms.getId().substring(0, index);
        Class<?> cla = Class.forName(className);
        // 没有注解不过滤
        if (!cla.isAnnotationPresent(XzcodeFilter.class)) {
            return invocation.proceed();
        }

        String methodName = (ms.getId().substring(index + 1));
        if (methodName.contains("_")) {
            methodName = methodName.split("_")[0];
        }
        XzcodeFilter annotation = cla.getAnnotation(XzcodeFilter.class);
        String[] excludedMethods = annotation.excludedMethods();
        Method[] methods = cla.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                // 例外的方法不过滤
                if (checkMethodIsExcluded(excludedMethods, methodName)) {
                    break;
                }
                Object parameter = args[1];
                Executor target = (Executor) invocation.getTarget();
                RowBounds rowBounds = (RowBounds) args[2];
                ResultHandler resultHandler = (ResultHandler) args[3];
                CacheKey cacheKey = null;
                BoundSql boundSql;
                if (args.length == 6) {
                    cacheKey = (CacheKey) args[4];
                    boundSql = (BoundSql) args[5];
                } else {
                    boundSql = ms.getBoundSql(invocation.getArgs()[1]);
                }
                String newSql = buildNewSql(boundSql);
                BoundSql newBoundSql =
                        new BoundSql(
                                ms.getConfiguration(),
                                newSql,
                                boundSql.getParameterMappings(),
                                parameter);
                setAdditionParameters(boundSql, newBoundSql);
                return target.query(ms, parameter, rowBounds, resultHandler, cacheKey, newBoundSql);
            }
        }
        return invocation.proceed();
    }

    private void setAdditionParameters(BoundSql boundSql, BoundSql newBoundSql) {
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            if (boundSql.hasAdditionalParameter(mapping.getProperty())) {
                newBoundSql.setAdditionalParameter(
                        mapping.getProperty(),
                        boundSql.getAdditionalParameter(mapping.getProperty()));
            }
        }
    }

    private boolean checkMethodIsExcluded(String[] exclusionMethods, String methodName) {
        if (StringUtils.isEmpty(methodName)) {
            return true;
        }
        for (String method : exclusionMethods) {
            if (methodName.equals(method)) {
                return true;
            }
        }
        return false;
    }

    private String buildNewSql(BoundSql boundSql) {
        String sql = boundSql.getSql();

        String aliasName = getAliasName(sql);
        String xzcode = userService.getXzcode(request);
        int whereIndex;
        whereIndex = sql.lastIndexOf("WHERE");
        if (whereIndex == -1) {
            whereIndex = sql.lastIndexOf("where");
        }
        if (whereIndex != -1) {
            whereIndex = handleExists(sql, whereIndex);
            StringBuilder builder = new StringBuilder();
            int index = whereIndex + 5;
            builder.append(sql, 0, index)
                    .append(" left(")
                    .append(aliasName)
                    .append("xzcode,")
                    .append(xzcode.length())
                    .append(") = '")
                    .append(xzcode)
                    .append("' and ")
                    .append(sql.substring(index));
            return builder.toString();
        }

        int groupByIndex;
        groupByIndex = sql.indexOf("GROUP BY");
        if (groupByIndex == -1) {
            groupByIndex = sql.indexOf("group by");
        }
        if (groupByIndex != -1) {
            return getAppendSql(sql, groupByIndex - 1, aliasName, xzcode);
        }

        int orderByIndex;
        orderByIndex = sql.indexOf("ORDER BY");
        if (orderByIndex == -1) {
            orderByIndex = sql.indexOf("order by");
        }
        if (orderByIndex != -1) {
            return getAppendSql(sql, orderByIndex - 1, aliasName, xzcode);
        }

        int limitIndex;
        limitIndex = sql.indexOf(" LIMIT ");
        if (limitIndex == -1) {
            limitIndex = sql.indexOf(" limit ");
        }
        if (limitIndex != -1) {
            return getAppendSql(sql, limitIndex, aliasName, xzcode);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(sql)
                .append(" WHERE left(")
                .append(aliasName)
                .append("xzcode,")
                .append(xzcode.length())
                .append(") = '")
                .append(xzcode)
                .append("'");
        return builder.toString();
    }

    private int handleExists(String sql, int whereIndex) {
        if (sql.contains("exists") || sql.contains("EXISTS")) {
            String subSql = sql.substring(0, whereIndex);
            if (sql.contains("exists") || sql.contains("EXISTS")) {
                whereIndex = subSql.lastIndexOf("where");
                if (whereIndex == -1) {
                    whereIndex = subSql.lastIndexOf("WHERE");
                }
            }
        }
        return whereIndex;
    }

    private String getAppendSql(String sql, int index, String aliasName, String xzcode) {
        StringBuilder builder = new StringBuilder();
        builder.append(sql, 0, index)
                .append(" where left(")
                .append(aliasName)
                .append("xzcode,")
                .append(xzcode.length())
                .append(") = '")
                .append(xzcode)
                .append("' ")
                .append(sql.substring(index));
        return builder.toString();
    }

    private String getAliasName(String sql) {
        return sql.contains(" dt ") ? "dt." : "";
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {}
}
