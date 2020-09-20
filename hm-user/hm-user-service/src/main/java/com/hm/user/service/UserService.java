package com.hm.user.service;

import com.hm.common.enums.ExceptionEnums;
import com.hm.common.exception.HmException;
import com.hm.user.mapper.UserMapper;
import com.hm.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

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
}
