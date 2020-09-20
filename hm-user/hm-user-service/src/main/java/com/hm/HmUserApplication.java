package com.hm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 注意这里引入的包mapperscan的包是1.01的。如果引入的包是1.3的不匹配会报错
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.hm.user.mapper")
public class HmUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(HmUserApplication.class,args);
    }
}
