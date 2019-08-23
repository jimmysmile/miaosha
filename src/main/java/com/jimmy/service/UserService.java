package com.jimmy.service;

import com.jimmy.error.BusinessException;
import com.jimmy.service.model.UserModel;
import org.springframework.stereotype.Service;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-21 19:05
 */
@Service
public interface UserService {
    //通过用户ID获取用户对象的方法
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
    /*
    telphone:用户手机
    password：用户加密后的密码
     */
    UserModel  validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
