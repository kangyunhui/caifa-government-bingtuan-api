package com.junyi.permission.interceptor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionConfig implements WebMvcConfigurer {

    private final @NonNull PermissionInterceptor permissionInterceptor;

    @Value("${junyi.permission.exclude-patterns:null}")
    private String[] excludePatterns;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/logo.*")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/**/*.html", "/**/*.html/**")
                .excludePathPatterns(
                        "/swagger-ui.html",
                        "/swagger-ui/*",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/webjars/**")
                .excludePathPatterns(excludePatterns);
    }
}
