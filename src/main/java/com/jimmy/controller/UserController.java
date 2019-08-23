package com.jimmy.controller;

import com.alibaba.druid.util.StringUtils;
import com.jimmy.controller.viewobject.UserVO;
import com.jimmy.error.BusinessException;
import com.jimmy.error.EnumBusinessError;
import com.jimmy.response.CommonReturnType;
import com.jimmy.service.UserService;
import com.jimmy.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * @Author: Administrator
 * @CreateTime: 2019-08-21 19:02
 */
@Controller
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;//内部有一个Threadlocal，所以不用担心到单例模式下只有一个session

   //用户登录接口
   @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
   @ResponseBody
   public CommonReturnType login(@RequestParam(name = "telphone")String telphone,@RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
       //入参校验
       if (org.apache.commons.lang3.StringUtils.isEmpty(telphone)|| org.apache.commons.lang3.StringUtils.isEmpty(password)){
           throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);

       }
       //用户登陆服务，用来校验用户登陆是否合法
       UserModel userModel = userService.validateLogin(telphone, this.EncodeByMd5(password));
       //将登陆凭证加入用户登陆成功的session内
       this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
       this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
       return CommonReturnType.create(null);
   }







    //用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //1.验证对应的手机号和otpcode是否符合
            String insessionOtpCode= (String) this.httpServletRequest.getSession().getAttribute(telphone);
            //使用druid的string工具类的equals方法，可判断为null的情况
            if (!StringUtils.equals(otpCode,insessionOtpCode)){
            throw  new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
            }

        //2.用户的注册流程
            UserModel userModel = new UserModel();
            userModel.setName(name);
            userModel.setAge(age);
            userModel.setGender(gender);
            userModel.setTelphone(telphone);
            userModel.setRegisterMode("byphone");
            //对密码进行加密
            userModel.setEncrptPassword(this.EncodeByMd5(password));
                userService.register(userModel);
                return CommonReturnType.create(null);
    }
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en=new BASE64Encoder();
        //加密字符串
        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    //
    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method ={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getotp(@RequestParam(name = "telphone") String telphone){
        //1.需要按照一定的规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);//[0,99999)
        randomInt+=10000;//[10000,109999)
        String otpCode=String.valueOf(randomInt);
        //2.将otp验证码同对应用户的手机关联,使用httpsession的方式来绑定电话号和otpcode
            httpServletRequest.getSession().setAttribute(telphone,otpCode);
        //3.将OTP验证码通过短信通道发送给用户 省略
        System.out.println("tele: " +telphone+" otpcode: "+otpCode);
        return CommonReturnType.create(null);
    }




    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel user = userService.getUserById(id);
        if (user==null){
            throw new BusinessException(EnumBusinessError.USER_NOT_EXIST);
        }
        //将核心用户模型对象转化为可供UI使用的viewobject对象
        UserVO userVO = convertFromModel(user);
        //返回通用对象
        return CommonReturnType.create(userVO);
    }
    public UserVO convertFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

 }
