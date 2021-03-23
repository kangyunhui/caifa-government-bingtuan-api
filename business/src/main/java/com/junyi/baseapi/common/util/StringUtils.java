package com.junyi.baseapi.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static boolean isEmpty(String str) {
        return str == null
                || str.length() <= 0
                || "null".equalsIgnoreCase(str)
                || "".equalsIgnoreCase(str);
    }

    public static boolean isNull(Object obj) {
        return obj == null && "".equals(obj);
    }

    public static boolean isNotNull(Object obj) {
        return obj != null && !"".equals(obj);
    }

    public static boolean isEqual(String str, String str2) {
        return str == str2 || str.equals(str2);
    }

    /**
     * 产生随机n位数
     *
     * @param n
     * @return
     */
    public static String getRandStr(int n) {
        Random random = new Random();
        String sRand = "";
        for (int i = 0; i < n; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
        }
        return sRand;
    }

    /**
     * 字符串转UTF-8编码
     *
     * @author Lxw
     */
    public static String StringToUTF8(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 0 && c <= 255) {
                stringBuffer.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    stringBuffer.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return stringBuffer.toString();
    }

    /** String转Double */
    public static Double StringToDouble(String str) {
        if (str == null || str.equals("") || str.equalsIgnoreCase("null") || str.length() <= 0) {
            return 0d;
        }
        return Double.parseDouble(str);
    }

    /**
     * 获取唯一ID
     *
     * @param type 1学生,2老师，3学校，4机构
     * @return
     */
    public static String getUniqueID(Integer type) {
        String str = "";
        if (type == 2) {
            str = "LS";
        } else if (type == 3) {
            str = "XX";
        } else if (type == 4) {
            str = "JG";
        } else {
            str = "";
        }
        Long num = System.currentTimeMillis() - 1300000000000L;
        return str + num;
    }

    /**
     * 产生随机n位数包含字母
     *
     * @param n
     * @return
     */
    public static String getRand(int n) {
        String a = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sRand = new StringBuffer("");
        for (int i = 0; i < n; i++) {
            char rand = a.charAt((int) (a.length() * Math.random()));
            sRand.append(rand);
        }
        return sRand.toString();
    }

    /**
     * 字符串数组去重和""
     *
     * @param ss
     * @return
     */
    public static String[] removeRepeatString(String[] ss) {
        // array_unique
        List<String> list = new ArrayList<String>();
        for (String s : ss) {
            if (!list.contains(s) && s != "") // 或者list.indexOf(s)!=-1
            list.add(s);
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 字符串集合去重和""
     *
     * @param arr
     * @return
     */
    public static List<String> removeRepeatList(List<String> arr) {
        List<String> list = new ArrayList<String>();
        if (arr.size() <= 0) {
            return arr;
        }
        for (String a : arr) {
            if (!list.contains(a) && !isEmpty(a)) {
                list.add(a);
            }
        }
        return list;
    }

    /**
     * 创建一个在范围内的随机数
     *
     * @param minVal
     * @param maxVal
     * @return
     * @author ztd
     */
    private static Integer createRandomKey(Integer minVal, Integer maxVal) {
        Integer v = new Random().nextInt(maxVal);
        if (v <= minVal) {
            v = v + minVal;
        }
        return v;
    }

    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /** 得到字符串中的图片的地址 */
    public static Set<String> getImgStr(String htmlStr) {
        Set<String> pics = new HashSet<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            System.out.println("img" + img);
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    /** 将字符串中的图片链接转成base64 */
    public static String replaceImgUrlToBase64Str(String htmlStr) {
        Set<String> pics = new HashSet<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();

            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics.toString();
    }

    // 将图片链接转成base64
    public static synchronized String getImageStrFromUrl(String imgURL) {
        byte[] data = null;
        try {
            // 创建URL
            URL url = new URL(imgURL);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            // 创建一个Buffer字符串
            byte[] buffer = new byte[inStream.available()];
            // 每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            // 使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {
                // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
            // inStream.read(data);
            inStream.close();
            /*Thread.sleep(200);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回Base64编码过的字节数组字符串
        return Base64.getEncoder().encodeToString(data);
    }

    // 将图片链接转成InputStream
    public static InputStream getInputStreamFromUrl(String urlImg) {
        try {
            // 创建URL
            URL url = new URL(urlImg);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            // System.out.println(inStream.toString());
            // inputStream.close();
            return inStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 阿拉伯数字转换成中文数字
    public static String intToCh(int d) {
        // String[] str = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String[] str = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        // String ss[] = new String[] { "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿" };
        String ss[] = new String[] {"", "十", "百", "千", "万", "十", "百", "千", "亿"};
        String s = String.valueOf(d);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            String index = String.valueOf(s.charAt(i));
            sb = sb.append(str[Integer.parseInt(index)]);
        }
        String sss = String.valueOf(sb);
        int i = 0;
        for (int j = sss.length(); j > 0; j--) {
            sb = sb.insert(j, ss[i++]);
        }
        if (sb.toString().length() == 2 || sb.toString().length() == 3) {
            if (sb.toString().startsWith("一")) {
                sb = new StringBuffer(sb.toString().substring(1, sb.toString().length()));
            }
        }
        if (sb.toString().endsWith("零") && sb.toString().length() > 1) {
            return sb.toString().substring(0, sb.toString().length() - 1);
        } else {
            return sb.toString();
        }
    }

    // Java字符串中可能有{#blank#}2{#/blank#}，如果有就替换为下划线
    public static String changeBlank(String data) {
        return data.replaceAll("\\{#blank#\\}\\d*\\{#/blank#\\}", "______");
    }

    // 让图片链接地址转img
    public static String urlToImgStr(String url) {
        return "<img src='" + url + "' width='665' />";
    }
}
