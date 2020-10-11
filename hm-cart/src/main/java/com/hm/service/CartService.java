package com.hm.service;


import com.hm.auth.entity.UserInfo;
import com.hm.common.enums.ExceptionEnums;
import com.hm.common.exception.HmException;
import com.hm.common.utils.JsonUtils;
import com.hm.filter.LoginInterceptor;
import com.hm.pojo.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * hm
 */
@Service
@Slf4j
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "ly:cart:uid:";

    /**
     * 添加购物车到Redis中
     *
     * @param cart
     */
    public void addCart(Cart cart) {
        //1 获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();

        //2 定义redis的key
        String key = KEY_PREFIX+loginUser.getId();

        //2.1 获取商品的key
        String hashKey = cart.getSkuId().toString();

        Integer num = cart.getNum();
        //2.2 创建redis的操作对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);

        //3 判断当前购物车中是否存在商品
//        if(hashOps.hasKey(hashKey)){
//            //3.1 获取原有商品的数量
//            Object o = hashOps.get(hashKey);
//            String json = o.toString();
//            Cart cacheCart = JsonUtils.parse(json, Cart.class);
//            cacheCart.setNum(cacheCart.getNum()+cart.getNum());
//            hashOps.put(hashKey,JsonUtils.toString(cacheCart));  //todo 这样写并不好，有冗于代码
//        }
//       hashOps.put(hashKey, JsonUtils.toString(cart));

        //3 判断当前购物车中是否存在商品
        if(hashOps.hasKey(hashKey)){
            String json = hashOps.get(hashKey).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum()+num);
        }
        hashOps.put(hashKey, JsonUtils.toString(cart));
    }

    public List<Cart> listCart() {
        //1 获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();

        //2 定义redis的key
        String key = KEY_PREFIX+loginUser.getId();
        //2.2 创建redis的操作对象
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);

//        List<Object> values = operation.values();
        //3 从redis中取出购物车的value值
        //todo 注意这种流的写法
        return operation.values().stream().map(o -> JsonUtils.toBean(o.toString(),Cart.class)).collect(Collectors.toList());
    }


    public void updateNum(Long skuid, Integer num) {
        //1 获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();

        //2 定义redis的key
        String key = KEY_PREFIX+loginUser.getId();
        //2.2 创建redis的操作对象
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        if (!operation.hasKey(skuid.toString())){
            throw new HmException(ExceptionEnums.CART_NOT_FOUND);
        }
        //3查询
        //todo 注意购物车的结构是redis的hash结构 即 Map<String,Map<String,String>> 所以根据skuid取出来的也是字符串，所以要转成cart对象
        Cart cart = JsonUtils.toBean(operation.get(skuid.toString()).toString(), Cart.class);

        //4 为当前购物车数量赋值
        cart.setNum(num);

        //5 将当前cart对象写回redis中
        //todo 注意因为redis的hash结构，所以写回到redis时要把cart转成string
        //todo 同时这里的key应该是底层map的key 即skuid（商品id）
        operation.put(skuid.toString(),JsonUtils.toString(cart));
    }

    public void deleteCart(Long skuId) {
        //1 获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();

        //2 定义redis的key
        String key = KEY_PREFIX+loginUser.getId();

        //3 删除购物车
        //todo 方法一
//        redisTemplate.opsForHash().delete(key,skuId.toString());

        //todo 方法二
        //2.2 创建redis的操作对象
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);

        if (!operation.hasKey(skuId.toString())) {
            //该商品不存在
            throw new RuntimeException("购物车商品不存在, 用户：" + loginUser.getId() + ", 商品：" + skuId);
        }
        operation.delete(skuId.toString());
    }
}
