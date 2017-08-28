package com.jetair.shopping.service;

import com.jetair.shopping.model.User;
import com.uni.webservice.service.sale.inter.ShoppingReq;
import com.uni.webservice.service.sale.inter.ShoppingRes;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloService {
    @WebMethod
    public User getUser(@WebParam(name = "username") String username);

    @WebMethod
    ShoppingRes shopping(@WebParam(name = "arg0",targetNamespace = "") ShoppingReq var1);

}
