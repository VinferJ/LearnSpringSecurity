package me.vinfer.jwtsecurity.exception;

import lombok.Getter;

/**
 * @author Vinfer
 * @date 2020-09-20    21:36
 * @description 请求超时异常
 **/
@Getter
public class RequestExpiredException extends RuntimeException{

    private final long expired;

    public RequestExpiredException(long expired){
        super("请求超时...超时时间："+expired+"s");
        this.expired = expired;
    }

}
