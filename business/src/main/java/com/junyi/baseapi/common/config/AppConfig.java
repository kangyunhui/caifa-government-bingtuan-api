package com.junyi.baseapi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author zhangxianshuai
 * @description 配置类
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${oss-path}")
    private String ossPath;

    @Bean("sessionStrategy")
    public SessionStrategy registBean() {
        SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
        return sessionStrategy;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/v1/oss/**").addResourceLocations("file:" + ossPath);
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/doc.html")
                .resourceChain(true);
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(true);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/").setViewName("forward:/swagger-ui/index.html");
    }
}
