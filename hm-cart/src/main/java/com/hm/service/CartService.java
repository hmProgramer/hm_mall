package com.hm.service;


import com.hm.auth.entity.UserInfo;
import com.hm.common.utils.JsonUtils;
import com.hm.filter.LoginInterceptor;
import com.hm.pojo.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
}
