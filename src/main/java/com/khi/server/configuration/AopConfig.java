package com.khi.server.configuration;

import com.khi.server.aop.MyAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public MyAspect myAspect() {
        return new MyAspect();
    }
}
