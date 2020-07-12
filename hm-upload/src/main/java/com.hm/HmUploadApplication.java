package com.hm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HmUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(HmUploadApplication.class, args);
    }
}
