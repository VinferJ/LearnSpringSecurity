package me.vinfer.jwtsecurity.exception;

import org.springframework.security.core.AuthenticationException;

import javax.security.auth.message.AuthException;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-24    11:41
 * @description
 **/
public class TokenExpiredException extends AuthenticationException {

    public TokenExpiredException(String msg){
        super(msg);
    }

}
