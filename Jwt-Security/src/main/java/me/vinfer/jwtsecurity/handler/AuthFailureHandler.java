package me.vinfer.jwtsecurity.handler;

import com.alibaba.fastjson.JSON;
import me.vinfer.jwtsecurity.constants.ErrorCode;
import me.vinfer.jwtsecurity.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * @author Vinfer
 * @date 2020-09-18    15:49
 * @description 认证失败时的处理器
 **/
public class AuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        String username = request.getParameter("username");
        LOG.info("登录认证失败：用户[{}]未通过认证....",username);
        if( exception instanceof UsernameNotFoundException){
            writer.write(JSON.toJSONString(ResponseUtil.error(ErrorCode.USER_NOT_FOUND)));
            return;
        }
        if (exception instanceof AccountStatusException){
            writer.write(JSON.toJSONString(ResponseUtil.error(ErrorCode.USER_NOT_AVAILABLE)));
            return;
        }
        writer.write(JSON.toJSONString(ResponseUtil.error(ErrorCode.WRONG_PASSWORD)));
    }
}
