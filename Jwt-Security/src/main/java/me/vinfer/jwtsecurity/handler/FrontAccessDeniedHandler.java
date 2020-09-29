package me.vinfer.jwtsecurity.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.vinfer.jwtsecurity.constants.ErrorCode;
import me.vinfer.jwtsecurity.service.auth.FrontUserDetails;
import me.vinfer.jwtsecurity.utils.ResponseUtil;
import me.vinfer.jwtsecurity.utils.SecurityContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    17:58
 * @description
 **/
@Slf4j
public class FrontAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        FrontUserDetails userDetails = SecurityContextUtil.user();
        log.info("用户[{}]权限不足", userDetails.getUsername());
        response.getWriter().write(JSON.toJSONString(ResponseUtil.error(ErrorCode.ACCESS_DENIED)));
    }
}
