package com.hm.user.controller;

import com.hm.user.pojo.User;
import com.hm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@RequestMapping("user")
@Controller
@RequestMapping("user")
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

    /**
     * 用户注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code) {
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        return ResponseEntity.ok(userService.queryUser(username, password));
    }


    /**
     * 添加用户名及密码
     * @param
     * @param
     * @return
     */
    @PostMapping("addUser")
    public ResponseEntity<Void> addUser(@RequestParam("username")String username, @RequestParam("password")String password) {
        userService.addUser(username, password);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
