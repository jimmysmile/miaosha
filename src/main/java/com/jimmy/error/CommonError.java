package com.jimmy.error;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-22 00:51
 */
public interface CommonError {
    int getErrorCode();
    String getErrorMessage();
    CommonError setErrMsg(String errMsg);
}
