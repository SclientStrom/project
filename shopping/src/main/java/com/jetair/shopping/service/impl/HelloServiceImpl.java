package com.jetair.shopping.service.impl;

import com.jetair.shopping.model.User;
import com.jetair.shopping.service.HelloService;
import com.uni.webservice.service.sale.inter.IUniSaleShoppingService;
import com.uni.webservice.service.sale.inter.ShoppingReq;
import com.uni.webservice.service.sale.inter.ShoppingRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloServiceImpl implements HelloService {
    @Autowired
    private IUniSaleShoppingService saleShoppingService;
    @Override
    public User getUser(String username) {
        User user=new User();
        user.setName(username);
        user.setAge("234");
        return user;
    }

    @Override
    public ShoppingRes shopping(ShoppingReq var1) {
        return saleShoppingService.shopping(var1);
    }
}
