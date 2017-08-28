package com.jetair.shopping.collector.impl;

import com.jetair.shopping.collector.ShoppingCollector;
import com.uni.webservice.service.sale.inter.IUniSaleShoppingService;
import com.uni.webservice.service.sale.inter.ShoppingReq;
import com.uni.webservice.service.sale.inter.ShoppingRes;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCollectorImpl implements ShoppingCollector {
    @Autowired
    private IUniSaleShoppingService saleShoppingService;


    private ShoppingRes saleShoppingRes(ShoppingReq req){
        return this.saleShoppingService.shopping(req);
    }
}
