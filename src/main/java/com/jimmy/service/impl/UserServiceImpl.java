package com.jimmy.service.impl;

import com.jimmy.dao.UserDOMapper;
import com.jimmy.dao.UserPasswordDOMapper;
import com.jimmy.dataobject.UserDO;
import com.jimmy.dataobject.UserPasswordDO;
import com.jimmy.error.BusinessException;
import com.jimmy.error.EnumBusinessError;
import com.jimmy.service.UserService;
import com.jimmy.service.model.UserModel;
import com.jimmy.validator.ValidationResult;
import com.jimmy.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-21 19:06
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;
    @Override
    public UserModel getUserById(Integer id) {
        //调用UserDOMapper返回用户对象
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO==null){
        return null;
        }
        //通过用户id获取对应加密的用户密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);
        return convertFromDataObject(userDO,userPasswordDO);

    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel==null){
            throw  new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //if (StringUtils.isEmpty(userModel.getName())
        //        || userModel.getGender() == null
        //        || userModel.getAge() == null
        //        || StringUtils.isEmpty(userModel.getTelphone())) {
        //    throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
        //
        //}
        ValidationResult validationResult=validator.validate(userModel);
        if (validationResult.isHasErrors()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrorMag());
        }
        //实现model到dataobject的方法
        UserDO userDO = convertFromUsermodel(userModel);
        //使用insertSelective(),是因为对应的sql语句会加非空的判断
        try {
            userDOMapper.insertSelective(userDO);
        }catch (Exception e){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }

        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromUsermodel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
        return;

    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if (userDO==null){
            throw new BusinessException(EnumBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);
        //比对用户加密的信息是否与传进来的密码相匹配
        if (!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw  new BusinessException(EnumBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    //实现返回usermodel
    private UserDO convertFromUsermodel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }
    //实现获取密码对象的方法
    private UserPasswordDO convertPasswordFromUsermodel(UserModel userModel){
        if (userModel == null) {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if (userDO==null){
            return null;
        }
        UserModel userModel=new UserModel();
        //将userDO的属性赋给userModel
        BeanUtils.copyProperties(userDO,userModel);
        if (userPasswordDO!=null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }

        return userModel;
    }
}
