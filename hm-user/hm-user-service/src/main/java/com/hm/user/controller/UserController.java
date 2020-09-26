package com.hm.user.controller;

import com.hm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private  StringRedisTemplate redisTemplate;

    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable( "type") Integer type) {
        boolean userStatus = userService.checkData(data, type);
        return ResponseEntity.ok(userStatus);
    }

    @GetMapping("test")
    public String checkUserData() {
        redisTemplate.opsForValue().set("test","hello world");
        String test = redisTemplate.opsForValue().get("test");
        System.out.println("test = " + test);
        return "1";
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @PostMapping("send")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone")String phone) {
        userService.sendVerifyCode(phone);
        return ResponseEntity.noContent().build();
    }

}
