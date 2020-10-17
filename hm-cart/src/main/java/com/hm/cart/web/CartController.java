package com.hm.cart.web;

import com.hm.cart.pojo.Cart;
import com.hm.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Cart>> listCart() {
        return ResponseEntity.ok(cartService.listCart());
    }

    /**
     * g更新购物车数量
     * @param id
     * @param num
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("id") Long id, @RequestParam("num") Integer num) {
        cartService.updateNum(id, num);
        return ResponseEntity.ok().build();
    }

    /**
     * 从购物车中删除商品
     *
     * @param id
     * @return
     */
    @DeleteMapping("id")
    public ResponseEntity<Void> deleteCart(@PathVariable("id") Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.ok().build();
    }
}
