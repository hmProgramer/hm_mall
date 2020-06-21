package com.hm.itemController;

import com.hm.common.enums.ExceptionEnums;
import com.hm.common.exception.HmException;
import com.hm.item.pojo.Item;
import com.hm.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ItemController {

    @Autowired
    private ItemService itemService;

    //todo 问题：responseBody  和responseEntry的区别

    @PostMapping("item")
    public ResponseEntity<Item> saveItem(Item item){
        if(item.getPrice() ==null){
            //返回空
            //todo 统一异常处理：把所有的异常拦截下来自己处理---》这是一种aop的思想
            //如何做？
            //1 在common包里创建commonExceptionHandler  加上注解@ControllerAdvice
            throw new HmException(ExceptionEnums.PRICE_CANNOT_BE_NULL);
           // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //如果有值就返回状态码和实体
        item = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping("item1")
    public String getData(){
        return "123";
    }
}
