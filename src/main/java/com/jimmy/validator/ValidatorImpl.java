package com.jimmy.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Author: Administrator
 * @CreateTime: 2019-08-22 23:22
 */
@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;

    //实现校验方法返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if (constraintViolationSet.size()>0){
            //说明有错误
            result.setHasErrors(true);
            constraintViolationSet.forEach(constraintViolation->{
                String errsg=constraintViolation.getMessage();
                //出错位置
                String propertyName=constraintViolation.getPropertyPath().toString();
                result.getErrorMsgMap().put(propertyName,errsg);
            });
        }
        return  result;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        //将validator通过工厂的方式实例化
        this.validator= Validation.buildDefaultValidatorFactory().getValidator();
    }
}
