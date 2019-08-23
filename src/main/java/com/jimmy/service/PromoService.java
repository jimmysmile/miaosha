package com.jimmy.service;

import com.jimmy.service.model.PromoModel;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-23 15:41
 */
public interface PromoService {
    PromoModel getPromoByitemId(Integer itemId);
}
