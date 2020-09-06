package com.hm.mistakes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by HM on 2020/6/14.
 */
@RestController
@SpringBootApplication
@EnableDiscoveryClient
public class HmItemMistakesApplication {
    public static void main(String[] args) {
        SpringApplication.run(HmItemMistakesApplication.class, args);
    }


}
