package me.vinfer.jwtsecurity.exception;

/**
 * @author Vinfer
 * @date 2020-09-20    20:33
 * @description aes加密解密的运行时异常
 **/
public class CryptException extends RuntimeException{

    public CryptException(String msg){
        super(msg);
    }

}
