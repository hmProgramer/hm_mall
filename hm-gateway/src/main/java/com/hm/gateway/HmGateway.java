package com.hm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by HM on 2020/6/14.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class HmGateway {
    public static void main(String[] args) {
        SpringApplication.run(HmGateway.class, args);
    }
}
