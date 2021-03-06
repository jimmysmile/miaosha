package com.jimmy.service.impl;

import com.jimmy.dao.ItemDOMapper;
import com.jimmy.dao.ItemStockDOMapper;
import com.jimmy.dataobject.ItemDO;
import com.jimmy.dataobject.ItemStockDO;
import com.jimmy.error.BusinessException;
import com.jimmy.error.EnumBusinessError;
import com.jimmy.service.ItemService;
import com.jimmy.service.PromoService;
import com.jimmy.service.model.ItemModel;
import com.jimmy.service.model.PromoModel;
import com.jimmy.validator.ValidationResult;
import com.jimmy.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validation;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-23 09:37
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;
    @Autowired
    private PromoService promoService;
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //入参校验
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrorMag());
        }
        //把itemModel转化为dataObject
        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);

        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());

        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        //返回创建完成后数据库里的对象
        return this.getItemById(itemModel.getId());
    }

    private ItemDO convertItemDOFromItemModel(ItemModel itemModel){
       if (itemModel==null){
        return  null;
       }
       ItemDO itemDO=new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }
    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList=itemDOMapper.listItem();
       List<ItemModel>  itemModelList= itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO=itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel=this.convertModelFromDataObject(itemDO,itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO==null){
            return  null;
        }
        //操作获得库存数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        //将dataobject转化成model
        ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);
        //获取活动商品信息
        PromoModel promoModel = promoService.getPromoByitemId(itemModel.getId());
        if (promoModel != null&&promoModel.getStatus()!=3) {
        itemModel.setPromoModel(promoModel);

        }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affectedRow = itemStockDOMapper.decreaseStock(itemId, amount);
        if (affectedRow>0){
            //对应更新库存成功
            return true;
        }else {
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
            itemDOMapper.increaseSales(itemId,amount);
    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
