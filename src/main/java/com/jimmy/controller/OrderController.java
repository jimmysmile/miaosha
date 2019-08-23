package com.jimmy.controller;

import com.jimmy.error.BusinessException;
import com.jimmy.error.EnumBusinessError;
import com.jimmy.response.CommonReturnType;
import com.jimmy.service.OrderService;
import com.jimmy.service.model.OrderModel;
import com.jimmy.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-23 14:24
 */
@Controller("/order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    //封装下单请求
    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId" ,required = false) Integer promoId) throws BusinessException {
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin==null||!isLogin.booleanValue()){
         throw new BusinessException(EnumBusinessError.USER_NOT_LOGIN);
        }
        //获取用户的登录信息
        UserModel userModel= (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.creatOrder(userModel.getId(), itemId, promoId,amount);
        return CommonReturnType.create(null);
    }
}
