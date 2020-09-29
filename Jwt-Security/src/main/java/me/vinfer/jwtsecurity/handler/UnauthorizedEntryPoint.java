package me.vinfer.jwtsecurity.handler;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vinfer.jwtsecurity.constants.ErrorCode;
import me.vinfer.jwtsecurity.exception.TokenExpiredException;
import me.vinfer.jwtsecurity.provider.CacheProvider;
import me.vinfer.jwtsecurity.utils.ResponseUtil;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Vinfer
 * @date 2020-09-19    17:31
 * @description 未通过授权认证的入口类，记录并处理未通过认证的请求
 **/
@AllArgsConstructor
@Slf4j
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private final CacheProvider cacheProvider;

    /**
     * 如果授权失败会将异常回调到本方法中
     * 因此方法中主要就是对授权失败的场景可以做一个统一处理
     * @param request           需要认证授权的请求
     * @param response          http响应
     * @param authException     权限异常
     * @throws IOException      io异常
     * @throws ServletException serv异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String username = request.getParameter("username");
        if (username == null){
            log.info("访问授权未通过记录：用户未通过登录认证，无法进行授权");
        }
        log.info("访问授权未通过记录：用户[{}]无法通过认证，授权失败",username);
        /*
        * 由于异常处理advice对象与认证服务时并行的
        * 异常抛出后会先一步被advice捕获到，因此会在处理后直接返回response
        * 这就导致response.outputStream被关闭了，这里再进行response.getWriter()的话
        * 就会抛出IllegalStateException，并且该异常无法再被捕获
        * 所以要在获取writer时进行一个response是否commit的判断
        * 如果已经提前返回，那么这里不需要再获取writer进行返回
        * */
        if(!response.isCommitted()){
            PrintWriter writer = response.getWriter();
            String token = request.getHeader("Authorization");
            if(authException instanceof AccountStatusException){
                log.info("认证服务：授权失败，用户[{}]账户已被禁用",username);
                cacheProvider.removeLoginUser(token);
                writer.write(JSON.toJSONString(ResponseUtil.error(ErrorCode.USER_NOT_AVAILABLE)));
                return;
            }
            if (authException instanceof TokenExpiredException){
                log.info("用户认证token已过期：{}",authException.getMessage());
                writer.write(JSON.toJSONString(ResponseUtil.error(ErrorCode.TOKEN_EXPIRED)));
                return;
            }
            writer.write(JSON.toJSONString(ResponseUtil.error(ErrorCode.TOKEN_ERROR)));
        }
    }


}
