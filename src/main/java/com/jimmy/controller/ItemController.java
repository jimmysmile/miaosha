package com.jimmy.controller;

import com.jimmy.controller.viewobject.ItemVO;
import com.jimmy.error.BusinessException;
import com.jimmy.response.CommonReturnType;
import com.jimmy.service.ItemService;
import com.jimmy.service.model.ItemModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-23 10:23
 */
@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController {
        @Autowired
        private ItemService itemService;
    //创建商品的controller
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType creatItem(@RequestParam(value = "title")String title,
                                      @RequestParam(value = "description") String description,
                                      @RequestParam(value = "price") BigDecimal price,
                                      @RequestParam(value = "stock") Integer stock,
                                      @RequestParam(value = "imgUrl") String imgUrl) throws BusinessException {
        //封装service来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemVO itemVO = convertVOFromModel(itemModelForReturn);


        return CommonReturnType.create(itemVO);

    }
    //商品详情页浏览
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public  CommonReturnType getItem(@RequestParam(value = "id") Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }
    //商品列表页面浏览
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem() {
        List<ItemModel> itemModels = itemService.listItem();
        //使用stream API将list内的itemModel转化为ITEMVO
        List<ItemVO> itemVOList = itemModels.stream().map(itemModel -> {
            ItemVO itemVO = this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);
    }
  private ItemVO convertVOFromModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
      ItemVO itemVO = new ItemVO();
      BeanUtils.copyProperties(itemModel,itemVO);
      if (itemModel.getPromoModel()!=null){
          //有正在进行或即将进行的秒杀活动
          itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
          itemVO.setPromoId(itemModel.getPromoModel().getId());
          itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
          itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
      }else {
          itemVO.setPromoStatus(0);
      }
      return itemVO;
  }
}
