package me.vinfer.jwtsecurity.exception;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-23    15:22
 * @description
 **/
public class BusinessException extends RuntimeException{

    public BusinessException(String msg){
        super(msg);
    }

}
