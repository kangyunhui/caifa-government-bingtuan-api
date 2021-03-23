package com.junyi.baseapi.common.util;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author zhangxianshuai
 * @create 2019-11-30 20:32
 * @description http请求工具类
 */
@Slf4j
public class HttpsRequestUtil {

    public static String post(JSONObject json, String strUrl, String token) {
        String params = json.toString();
        System.out.println(params);
        BufferedReader reader;
        try {
            URL url = new URL(strUrl); // 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            if (token != null) {
                connection.setRequestProperty("Authorization", token); // 携带请求token
            }
            connection.connect();
            // 一定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
            if (null != params) {
                OutputStream outputStream = connection.getOutputStream();
                // 注意编码格式
                outputStream.write(params.getBytes("UTF-8"));
                outputStream.close();
            }
            // 读取响应
            reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();
            System.out.println(res);
            return res;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "error"; // 自定义错误信息
    }

    public static String get(String strUrl, String token) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null; // 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(strUrl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            if (token != null) {
                connection.setRequestProperty("Authorization", token); // 携带请求token
            }
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                System.out.println(result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect(); // 关闭远程连接
        }

        return result;
    }

    public static String getImg(String strUrl, String token, String ossPath, String commonPath) {
        String fileName = CommonUtils.getUUID() + strUrl.substring(strUrl.lastIndexOf('.'));
        HttpURLConnection connection = null;
        InputStream is = null;
        File file = new File(ossPath + commonPath + "/" + fileName);

        String result = null; // 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(strUrl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            if (token != null) {
                connection.setRequestProperty("Authorization", token); // 携带请求token
            }
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                byte[] bs = new byte[1024];
                int len;
                // 检测是否存在目录
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream os = new FileOutputStream(file, true);
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                os.close();
                is.close();
                result = commonPath + "/" + fileName;
                System.out.println(result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect(); // 关闭远程连接
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        /*String token = "Bearer pcmeyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJlY2hpc2FuIiwic3ViIjoibGlubm9uZ19uYW1lIiwiaWF0IjoxNTc2ODMyODE0LCJleHAiOjE1NzY4NDcyMTR9.R4B0c959dxeuJUCwFtc40pUqYpkXsuEaUH1NPYmtas_1b33isVRhGyFrWe2hpsDFgZCOOix1g-OB-tJWwENI7g";
        String url = "http://127.0.0.1:8062/api/v1/oss/33/aaa.png";
        System.out.println(getImg(url, token, "", "C:\\uploadFile\\33\\44"));*/
    }
}
