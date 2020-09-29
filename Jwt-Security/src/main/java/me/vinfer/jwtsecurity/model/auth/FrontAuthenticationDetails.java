package me.vinfer.jwtsecurity.model.auth;

import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinfer
 * @date 2020-09-20    02:21
 * @description 与Web身份验证请求相关的所选HTTP详细信息的持有者，基于该对象进行认证授权的流程，
 *              就是将一个http请求和自定义的AuthenticationDetails一起封装成一个WebAuthenticationDetails对象
 **/
@Getter
public class FrontAuthenticationDetails extends WebAuthenticationDetails {

    private final String username;
    private final String password;

    public FrontAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        if (username == null){
            username = "";
        }
        if (password == null){
            password = "";
        }
        this.username = username;
        this.password = password;
    }

    protected String obtainUsername(HttpServletRequest request){
        return request.getParameter("username");
    }

    protected String obtainPassword(HttpServletRequest request){
        return request.getParameter("password");
    }

}
