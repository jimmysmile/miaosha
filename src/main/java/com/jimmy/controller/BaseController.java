package com.jimmy.controller;

import com.jimmy.error.BusinessException;
import com.jimmy.error.EnumBusinessError;
import com.jimmy.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-22 01:41
 */
public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
   // //定义exceptionhandler解决未被controller吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest httpServletRequest, Exception e) {
        Map<String, Object> responseData = new HashMap<>();
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            responseData.put("errCode", businessException.getErrorCode());
            responseData.put("errMsg", businessException.getErrorMessage());
        } else {
            //只能将get封装起来，如果直接返回到create方法里会默认是UNKNOWN_ERROR,达不到get信息的效果
            responseData.put("errCode", EnumBusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errMsg", EnumBusinessError.UNKNOWN_ERROR.getErrorMessage());
        }
        return CommonReturnType.create(responseData, "fail");
    }
}
