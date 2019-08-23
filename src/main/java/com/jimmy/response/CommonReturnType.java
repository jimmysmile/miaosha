package com.jimmy.response;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-21 20:18
 * 规定返回类型
 */

public class CommonReturnType {
    //表明处理请求的返回处理结果 success或fail
    private String status;
    //若data=success，则data内返回前端所需要的json数据
    //若data=fail，则data内使用通用的错误码格式
    private  Object data;
    //定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
