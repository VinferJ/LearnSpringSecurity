package me.vinfer.jwtsecurity.advice;

import lombok.extern.slf4j.Slf4j;
import me.vinfer.jwtsecurity.constants.ErrorCode;
import me.vinfer.jwtsecurity.exception.BusinessException;
import me.vinfer.jwtsecurity.exception.DecryptBodyFailException;
import me.vinfer.jwtsecurity.exception.RequestExpiredException;
import me.vinfer.jwtsecurity.exception.TokenExpiredException;
import me.vinfer.jwtsecurity.utils.ResponseUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Vinfer
 * @date 2020-09-20    21:47
 * @description 异常捕获与处理的advice对象，这里进行异常全局异常的捕获与处理
 **/
@RestControllerAdvice()
@Slf4j
public class ExceptionAdvice {

    public static final String REG_ERROR_KEY = "注册";

    /**
     * 对DecryptBodyFailException类型的异常进行全局捕获处理
     * @param ex    解密失败的异常
     * @return      返回解密错误的响应
     */
    @ExceptionHandler(DecryptBodyFailException.class)
    public Object decryptBodyFailExceptionHandler(DecryptBodyFailException ex){
        log.error(ex.getMessage());
        return ResponseUtil.error(ErrorCode.REQUEST_ERROR);
    }


    /**
     * 对RequestExpiredException类型的异常进行全局捕获处理
     * @param ex    请求超时异常
     * @return      返回请求超时的响应
     */
    @ExceptionHandler(RequestExpiredException.class)
    public Object requestExpiredExceptionHandler(RequestExpiredException ex){
        log.error("请求超时，超时时间：{}s",ex.getExpired());
        return ResponseUtil.error(ErrorCode.REQUEST_EXPIRED);
    }

    @ExceptionHandler(IllegalStateException.class)
    public void illegalStateException(IllegalStateException ex){
        log.error("发生状态异常: {}",ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Object businessExceptionHandler(BusinessException ex){
        log.error("业务操作异常：{}",ex.getMessage());
        return ResponseUtil.error(ErrorCode.BUSINESS_ERROR);
    }


}
