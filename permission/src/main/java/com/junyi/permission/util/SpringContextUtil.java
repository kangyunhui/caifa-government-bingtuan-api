package com.junyi.permission.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author erma66.feng
 * @email erma66@sina.cn
 * @date 2021-02-16
 * @description xxxx
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @param beanName bean名稱 @Description: 獲取spring容器中的bean, 通過bean名稱獲取
     * @return: Object 返回Object,需要做強制類型轉換
     * @author: xuefb
     * @time: 2020-11-11 14:13:16
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * @param beanClass bean 類型 @Description: 獲取spring容器中的bean, 通過bean類型獲取
     * @return: T 返回指定類型的bean實例
     * @author: xuefb
     * @time: 2020-11-11 14:13:16
     */
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    /**
     * @param beanName bean 名稱
     * @param beanClass bean 類型 @Description: 獲取spring容器中的bean, 通過bean名稱和bean類型精確獲取
     * @return: T 返回指定類型的bean實例
     * @author: xuefb
     * @time: 2020-11-11 14:13:16
     */
    public static <T> T getBean(String beanName, Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }
}
