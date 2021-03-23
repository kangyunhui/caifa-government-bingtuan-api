package com.junyi.baseapi.common.util;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通过泛型和反射自动Mapper数据
 *
 * <p>Title: MapperUtil.java
 *
 * <p>Description:
 *
 * <p>Copyright: Copyright (c) 2016
 *
 * <p>Company: YunGe
 *
 * @author L
 * @version 1.0
 * @date 2017年5月5日 下午4:35:58
 */
@Slf4j
public class MapperUtil {

    /**
     * 利用反射将map集合封装成bean对象
     *
     * @param clazz
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<?> clazz) {
        try {
            Object obj = clazz.newInstance();
            if (!CollectionUtils.isEmpty(map)) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String propertyName = entry.getKey(); // 属性名
                    if (isAcronym(propertyName)) {
                        propertyName = propertyName.toLowerCase();
                    }
                    Object value = entry.getValue(); // 属性值
                    String setMethodName =
                            "set"
                                    + propertyName.substring(0, 1).toUpperCase()
                                    + propertyName.substring(1);
                    Field field = getClassField(clazz, propertyName); // 获取和map的key匹配的属性名称

                    if (field == null || value == null) {
                        continue;
                    }
                    Class<?> fieldTypeClass = field.getType();
                    value = convertValType(value, fieldTypeClass);
                    clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
                }
            }
            return (T) obj;
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new IllegalArgumentException(String.format("类型转换错误, %s", e.getMessage()));
        }
    }

    /**
     * 根据给定对象类匹配对象中的特定字段
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    private static Field getClassField(Class<?> clazz, String fieldName) {
        if (Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        Class<?> superClass = clazz.getSuperclass(); // 如果该类还有父类，将父类对象中的字段也取出
        if (superClass != null) { // 递归获取
            return getClassField(superClass, fieldName);
        }
        return null;
    }

    /**
     * 将map的value值转为实体类中字段类型匹配的方法
     *
     * @param value
     * @param fieldTypeClass
     * @return
     */
    private static Object convertValType(Object value, Class<?> fieldTypeClass) {
        Object retVal = null;

        if (Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if (Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if (Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            if (value == null) {
                value = 0;
            }
            retVal = Double.parseDouble(value.toString());
        } else if (BigDecimal.class.getName().equals(fieldTypeClass.getName())) {
            retVal = new BigDecimal(value.toString());
        } else if (Date.class.getName().equals(fieldTypeClass.getName())) {
            retVal = DateUtils.getStringToDate(value.toString());
        } else {
            retVal = value;
        }
        return retVal;
    }

    public static List mapListToBeanList(List<Map<String, Object>> mapList, Class<?> clazz) {
        return mapList.stream().map(map -> mapToBean(map, clazz)).collect(Collectors.toList());
    }

    /**
     * 判断字符串大小写
     *
     * @param word
     * @return
     */
    public static boolean isAcronym(String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }
}
