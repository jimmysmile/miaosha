package com.jimmy.service.model;

import java.math.BigDecimal;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-23 11:50
 */
//用户下单的交易模型
public class OrderModel {

    private String id;
    //购买的用户Id
    private  Integer userId;
    //购买的商品Id
    private Integer itemId;
    //若非空 则表示是以秒杀活动价格开始
    private Integer promoId;


    //购买的商品单价 若promoid非空 则表示秒杀商品价格
    private BigDecimal itemPrice;
    //购买数量
    private Integer amount;
    //购买金额 若promoid非空 则表示秒杀商品价格
    private BigDecimal orderPrice;

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }



    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
