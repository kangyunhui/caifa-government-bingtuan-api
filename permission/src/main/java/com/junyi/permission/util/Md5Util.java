package com.junyi.permission.util;

import com.junyi.permission.exception.PermissionException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Md5Util {

    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] s = md.digest();
            StringBuilder result = new StringBuilder();
            for (byte b : s) {
                result.append(Integer.toHexString((0x000000ff & b) | 0xffffff00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            throw new PermissionException("MD5加密出现错误");
        }
    }

    public static void main(String[] wer) {
        System.out.println(getMD5(getMD5("123456") + "92eef3f801"));
    }
}
