package com.springall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author york
 * @since 2021/7/15
 * com.springall.config
 * Cfg
 */
@Configuration
public class Cfg {

    @Bean
    public MyService myService() {
        return new MyService();
    }
}
