package me.vinfer.jwtsecurity.handler;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import me.vinfer.jwtsecurity.provider.CacheProvider;
import me.vinfer.jwtsecurity.utils.ResponseUtil;
import me.vinfer.jwtsecurity.utils.SecurityContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    18:10
 * @description
 **/
@AllArgsConstructor
public class FrontLogoutSuccessHandler implements LogoutSuccessHandler {

    private final CacheProvider cacheProvider;
    public static final Logger LOG = LoggerFactory.getLogger(FrontLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //从请求头拿到token信息
        String token = request.getHeader("Authorization");
        //拿到用户名信息
        String username = SecurityContextUtil.user().getUsername();
        //移除对应的token
        cacheProvider.removeLoginUser(token);
        /*更新登录状态，0表示登出*/
        Map<String,Object> stateMap = new HashMap<>(8);
        stateMap.put("state", 0);
        stateMap.put("token", null);
        cacheProvider.updateLoginState(username, stateMap);

        LOG.info("用户[{}]已退出登录",username);
        response.getWriter().write(JSON.toJSONString(ResponseUtil.emptySuccess()));
    }
}
