package me.vinfer.jwtsecurity.exception;

import lombok.Getter;

/**
 * @author Vinfer
 * @date 2020-09-20    21:42
 * @description 解密请求提示失败的运行时异常
 **/
@Getter
public class DecryptBodyFailException extends RuntimeException{

    public DecryptBodyFailException(String msg){
        super(msg);
    }

}
