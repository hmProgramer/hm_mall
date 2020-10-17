package com.hm.item.service;

import com.hm.common.utils.JsonUtils;
import com.hm.item.dto.CartDto;
import com.hm.item.service.GoodsService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * 注意当前测试类要和HmItemServiceApplication（启动类）在一样的目录层级下或者其子目录下
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodServiceTest {

    @Autowired
    private GoodsService goodsService;

    @org.junit.Test
    public void decreaseStock(){
        List<CartDto> cartDtoList = Arrays.asList(new CartDto(2600242L, 4), new CartDto(2600248L, 3));
        goodsService.decreaseStock(cartDtoList);
    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("phone","185");
        map.put("code","123");
        String remove = map.remove("phone");
        String s = JsonUtils.toString(map);
        System.out.println(remove);
        System.out.println(s);

    }
}
