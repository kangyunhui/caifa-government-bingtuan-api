package com.junyi.baseapi;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@EnableSwagger2
@ServletComponentScan
@ComponentScan(basePackages = {"com.junyi"})
@MapperScan(basePackages = {"com.junyi.baseapi.mapper", "com.junyi.permission.mapper"})
public class BaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApiApplication.class, args);
    }
}
