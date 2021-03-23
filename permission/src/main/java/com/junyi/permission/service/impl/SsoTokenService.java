package com.junyi.permission.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.junyi.permission.exception.PermissionException;
import com.junyi.permission.service.InterfaceService;
import com.junyi.permission.service.TokenService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedList;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(prefix = "junyi.user", name = "pattern", havingValue = "sso")
public class SsoTokenService implements TokenService {

    @Value("${junyi.sso.url:null}")
    private String ssoUrl;

    private final @NonNull InterfaceService interfaceService;

    private static final String LOGINOUT = "/loginout";
    private static final String AUTH_TOKEN = "/sso/verify";

    /** 验证Cookie @Return 返回请求结果 */
    private boolean authToken(String token) {
        String result = null; // 请求结果
        CloseableHttpClient client = HttpClients.createDefault();
        // 封装cookie作为请求参数
        LinkedList<NameValuePair> linkedList = new LinkedList<NameValuePair>();
        BasicNameValuePair tokenParam = new BasicNameValuePair(ACCESS_TOKEN, token);
        linkedList.add(tokenParam);

        try {
            URIBuilder uriBuilder = new URIBuilder(ssoUrl + AUTH_TOKEN);
            uriBuilder.setParameters(linkedList);
            HttpGet get = new HttpGet(uriBuilder.toString());
            result = doGet(client, get);
            if (StringUtils.isEmpty(result)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("建立请求异常");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                log.info("httpclient关闭失败");
            }
        }
        return Boolean.parseBoolean(JSONObject.parseObject(result).get("success").toString());
    }

    /**
     * 登出
     *
     * @return
     */
    @Override
    public void expireToken(String token) {
        String response = doGetResponse(token, LOGINOUT);
        if (StringUtils.isEmpty(response)
                || 200 != JSONObject.parseObject(response).getInteger("code")) {
            throw new PermissionException("登出失败");
        }
    }

    /** 发送接收get请求 */
    private String doGet(CloseableHttpClient client, HttpGet get) {
        try (CloseableHttpResponse response = client.execute(get)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean validate(String token, String url) {
        if (interfaceService.matchPublicInterface(url)) {
            return true;
        }
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        return authToken(token);
    }

    private String doGetResponse(String token, String url) {
        String result = null; // 请求结果
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder(ssoUrl + url);
            HttpGet get = new HttpGet(uriBuilder.toString());
            get.addHeader(ACCESS_TOKEN, token);
            result = doGet(client, get);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("建立请求异常");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                log.info("httpclient关闭失败");
            }
        }
        return result;
    }

    @Override
    public String getToken(String userId, String xzcode) {
        return null;
    }
}
