package com.hm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by HM on 2020/6/14.
 */
@SpringBootApplication
@EnableEurekaServer
public class HmRegistry{
    public static void main(String[] args) {
        SpringApplication.run(HmRegistry.class, args);
    }
}
