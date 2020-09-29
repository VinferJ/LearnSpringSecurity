package me.vinfer.jwtsecurity.model.auth;

import lombok.NoArgsConstructor;
import me.vinfer.jwtsecurity.service.auth.FrontUserDetails;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinfer
 * @date 2020-09-18    15:13
 * @description 为给定的Web请求提供一个AuthenticationDetails对象*
 **/
@NoArgsConstructor
public class FrontAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest,FrontAuthenticationDetails> {


    @Override
    public FrontAuthenticationDetails buildDetails(HttpServletRequest context) {
        //将请求的上下文对象传入自定义的AuthenticationDetails对象中
        return new FrontAuthenticationDetails(context);
    }

}
