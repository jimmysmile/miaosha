package com.jimmy.error;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-22 01:03
 */
//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError {
    private CommonError commonError;
    //直接接收EnumBusinessError的传参用于构造业务异常
    public  BusinessException(CommonError commonError){
        super();
        this.commonError=commonError;
    }
    //接收自定义errMsg的方式构造业务异常

    public BusinessException(CommonError commonError,String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }
    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMessage() {
        return this.commonError.getErrorMessage();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
