package com.junyi.baseapi.common.sms;

import lombok.Getter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangmingyue
 * @description 请求代理
 */
@Component
public final class RestProxy {

    @Getter private final RestTemplate restTemplate;
    public final Map<String, String> headers;
    private static final String CONTENT_TYPE = "Content-Type";

    public RestProxy() {
        this.headers = new HashMap<>();
        this.restTemplate = new RestTemplate();
        setMessageConverters();
    }

    public RestProxy contentType(String value) {
        headers.put(CONTENT_TYPE, value);
        return this;
    }

    private <RequestBody, ResponseBody> ResponseEntity<ResponseBody> call(
            String url,
            HttpMethod method,
            HttpEntity<RequestBody> request,
            Class<ResponseBody> responseBodyType,
            Object... uriVariables) {
        return getRestTemplate().exchange(url, method, request, responseBodyType, uriVariables);
    }

    private <RequestBody, ResponseBody> ResponseEntity<ResponseBody> call(
            String url,
            HttpMethod method,
            HttpEntity<RequestBody> request,
            Class<ResponseBody> responseBodyType,
            Map<String, ?> uriVariables) {
        return getRestTemplate().exchange(url, method, request, responseBodyType, uriVariables);
    }

    public <ResponseBody> ResponseEntity<ResponseBody> get(
            String url, Class<ResponseBody> responseBodyType, Object... uriVariables) {
        HttpEntity<Void> request = new HttpEntity<>(null, getHeaders());
        return call(url, HttpMethod.GET, request, responseBodyType, uriVariables);
    }

    public <ResponseBody> ResponseEntity<ResponseBody> get(
            String url, Class<ResponseBody> responseBodyType, Map<String, ?> uriVariables) {
        HttpEntity<Void> request = new HttpEntity<>(null, getHeaders());
        return call(url, HttpMethod.GET, request, responseBodyType, uriVariables);
    }

    public <ResponseBody> ResponseEntity<ResponseBody> delete(
            String url, Class<ResponseBody> responseBodyType, Object... uriVariables) {
        HttpEntity<Void> request = new HttpEntity<>(null, getHeaders());
        return call(url, HttpMethod.DELETE, request, responseBodyType, uriVariables);
    }

    public <ResponseBody> ResponseEntity<ResponseBody> delete(
            String url, Class<ResponseBody> responseBodyType, Map<String, ?> uriVariables) {
        HttpEntity<Void> request = new HttpEntity<>(null, getHeaders());
        return call(url, HttpMethod.DELETE, request, responseBodyType, uriVariables);
    }

    public <RequestBody, ResponseBody> ResponseEntity<ResponseBody> post(
            String url,
            RequestBody body,
            Class<ResponseBody> responseBodyType,
            Object... uriVariables) {
        HttpEntity<RequestBody> request = new HttpEntity<>(body, getHeaders());
        return call(url, HttpMethod.POST, request, responseBodyType, uriVariables);
    }

    public <RequestBody, ResponseBody> ResponseEntity<ResponseBody> post(
            String url,
            RequestBody body,
            Class<ResponseBody> responseBodyType,
            Map<String, ?> uriVariables) {
        HttpEntity<RequestBody> request = new HttpEntity<>(body, getHeaders());
        return call(url, HttpMethod.POST, request, responseBodyType, uriVariables);
    }

    public <RequestBody, ResponseBody> ResponseEntity<ResponseBody> put(
            String url,
            RequestBody body,
            Class<ResponseBody> responseBodyType,
            Object... uriVariables) {
        HttpEntity<RequestBody> request = new HttpEntity<>(body, getHeaders());
        return call(url, HttpMethod.PUT, request, responseBodyType, uriVariables);
    }

    public <RequestBody, ResponseBody> ResponseEntity<ResponseBody> put(
            String url,
            RequestBody body,
            Class<ResponseBody> responseBodyType,
            Map<String, ?> uriVariables) {
        HttpEntity<RequestBody> request = new HttpEntity<>(body, getHeaders());
        return call(url, HttpMethod.PUT, request, responseBodyType, uriVariables);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        headers.clear();
        return httpHeaders;
    }

    /** 解决乱码 */
    private void setMessageConverters() {
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        converterList.remove(1); // 移除StringHttpMessageConverter
        HttpMessageConverter<?> converter =
                new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converterList.add(1, converter); // 添加utf StringHttpMessageConverter
        restTemplate.setMessageConverters(converterList);
    }
}
