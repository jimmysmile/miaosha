package com.jimmy.service.impl;

import com.jimmy.dao.PromoDOMapper;
import com.jimmy.dataobject.PromoDO;
import com.jimmy.service.PromoService;
import com.jimmy.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-23 15:45
 */
@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;
    @Override
    public PromoModel getPromoByitemId(Integer itemId) {
        //获取对应秒杀商品的信息
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
        //dataobject ->model
        PromoModel promoModel = convertFromDataObject(promoDO);
        if (promoModel==null){
            return null;
        }
        //判断当前时间是否秒杀活动即将开始或正在进行
        DateTime now =new DateTime();
        if (promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if (promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }
        return promoModel;
    }
    private PromoModel convertFromDataObject(PromoDO promoDO){
         if (promoDO==null){
             return null;
         }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice().doubleValue()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
