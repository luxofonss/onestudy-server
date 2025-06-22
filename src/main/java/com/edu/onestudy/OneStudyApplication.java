package com.edu.onestudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:errors.properties", encoding = "UTF-8")
@ComponentScan("com.edu")
@EnableCaching
@EnableAspectJAutoProxy
@EnableFeignClients(basePackages = "com.edu.onestudy.thirdparty")
public class OneStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneStudyApplication.class, args);
    }

}
