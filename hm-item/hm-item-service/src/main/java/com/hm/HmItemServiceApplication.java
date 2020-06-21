package com.hm;

import com.hm.item.pojo.Item;
import com.hm.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by HM on 2020/6/14.
 */
@RestController
@SpringBootApplication
@EnableDiscoveryClient
public class HmItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HmItemServiceApplication.class, args);
    }


}
