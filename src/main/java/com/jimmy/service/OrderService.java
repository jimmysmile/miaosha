package com.jimmy.service;

import com.jimmy.error.BusinessException;
import com.jimmy.service.model.OrderModel;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-23 13:22
 */
public interface OrderService {
    OrderModel creatOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;
}
