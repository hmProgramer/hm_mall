package com.hm.user.service;

import com.hm.common.enums.ExceptionEnums;
import com.hm.common.exception.HmException;
import com.hm.common.utils.NumberUtils;
import com.hm.user.mapper.UserMapper;
import com.hm.user.pojo.User;
import com.hm.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:code:";

    public boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new HmException(ExceptionEnums.INVALID_USER_DATA_TYPE);

        }
        return userMapper.selectCount(record) == 0;
    }

    public void sendVerifyCode(String phone) {
        //1 随机生成6位数字验证码
        String generateCode = NumberUtils.generateCode(6);
        String key = KEY_PREFIX + phone;

        //2 存入到redis中,  并设置有效期为5min -->为之后的登录校验准备
        redisTemplate.opsForValue().set(key,generateCode,5, TimeUnit.MINUTES);

        //3 向mq发送消息
        Map<String,String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", generateCode);
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", map);

    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);

        //首先根据用户名查询用户
        User user = userMapper.selectOne(record);

        if (user == null) {
            throw new HmException(ExceptionEnums.USER_NOT_EXIST);
        }

        //检验密码是否正确
        if (!StringUtils.equals(CodecUtils.md5Hex(password, user.getSalt()), user.getPassword())) {
            //密码不正确
            throw new HmException(ExceptionEnums.PASSWORD_NOT_MATCHING);
        }

        return user;
    }
}
