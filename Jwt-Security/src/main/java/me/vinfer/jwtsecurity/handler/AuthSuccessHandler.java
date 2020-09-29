package me.vinfer.jwtsecurity.handler;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import me.vinfer.jwtsecurity.constants.TimeConfigEnum;
import me.vinfer.jwtsecurity.provider.CacheProvider;
import me.vinfer.jwtsecurity.service.auth.FrontUserDetails;
import me.vinfer.jwtsecurity.utils.JwtUtil;
import me.vinfer.jwtsecurity.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Vinfer
 * @date 2020-09-18    15:20
 * @description 认证成功时的处理器
 **/
@AllArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final CacheProvider cacheProvider;

    private static final Logger LOG = LoggerFactory.getLogger(AuthSuccessHandler.class);


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = request.getParameter("username");
        LOG.info("登录认证成功：用户[{}]认证已通过...",username);
        //拿到已经授权认证的userDetails对象
        FrontUserDetails userDetails = (FrontUserDetails) authentication.getPrincipal();
        /*
        * 先判断是否为重复登录，如果是重复登录
        * 那么先删除旧的登录凭证，只使用最新的登录凭证
        * 保证单点登录，不允许多端多点登陆
        * */
        handleDuplicateLogin(userDetails.getUsername());
        long defaultExpireTime = TimeConfigEnum.TOKEN_EXPIRATION_TIME.getTime() * 1000L;
        Map<String,Object> userInfo = new HashMap<>(16);
        userInfo.put("id", userDetails.getId());
        userInfo.put("username", userDetails.getUsername());
        userInfo.put("password", userDetails.getPassword());
        userInfo.put("roles", userDetails.getAuthorities());
        String token = JwtUtil.encode(userDetails.getId().toString(),defaultExpireTime, userInfo);

        Map<String,Object> callback = new HashMap<>(8);
        Map<String,Object> stateMap = new HashMap<>(8);
        //将该token封装到map并返回个前端
        callback.put("token", token);
        //登录认证成功，将用户信息存储到本地的map中，key就是token
        cacheProvider.storeLoginUser(token,userInfo);
        stateMap.put("state", 1);
        stateMap.put("token", token);
        //保存该用户的登录状态
        cacheProvider.storeLoginState(userDetails.getUsername(), stateMap);
        //数据回写
        response.getWriter().write(JSON.toJSONString(ResponseUtil.successWithData(callback)));
    }

    private void handleDuplicateLogin(String username){
        //获取用户登录状态的map
        @SuppressWarnings("unchecked")
        Map<String,Object> stateMap = (Map<String,Object>)cacheProvider.getLoginState(username);
        if (stateMap != null){
            Integer loginState = (Integer) stateMap.get("state");
            /*
             * TODO 同一个客户端下去重复登录，不同的客户端之间允许重复登录
             *
             * 如果该用户已登录，那么将旧的登录令牌移除
             * 只允许用户单点登录
             * */
            if (1 == loginState){
                String token = (String) stateMap.get("token");
                cacheProvider.removeLoginUser(token);
            }
        }

    }


}
