package com.jimmy.service;

import com.jimmy.error.BusinessException;
import com.jimmy.service.model.ItemModel;

import java.util.List;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-23 09:34
 */
public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);
    //库存扣减
    boolean decreaseStock(Integer itemId, Integer amount)throws BusinessException;
    //商品销量增加
    void increaseSales(Integer itemId,Integer amount)throws BusinessException;
}
