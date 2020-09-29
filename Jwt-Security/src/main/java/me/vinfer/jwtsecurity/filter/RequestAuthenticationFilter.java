package me.vinfer.jwtsecurity.filter;

import me.vinfer.jwtsecurity.model.auth.JwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinfer
 * @date 2020-09-20    02:40
 * @description     登录请求以外的请求认证过滤
 **/
public class RequestAuthenticationFilter extends BasicAuthenticationFilter {

    private final List<RequestMatcher> noAuthRequestMatchers;

    public RequestAuthenticationFilter(AuthenticationManager authenticationManager,
                                       AuthenticationEntryPoint authenticationEntryPoint,
                                       String[] noAuthUrls) {
        super(authenticationManager, authenticationEntryPoint);
        this.noAuthRequestMatchers = initMatherList(noAuthUrls);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*
        * 如果当前匹配到的请求不是是认证白名单的请求，执行认证逻辑
        * */
        if (this.requiresAuthenticate(request)) {
            //从请求头中获取token信息
            String token = request.getHeader("Authorization");

            if (token == null) {
                token = "";
            }
            try {
                //初始化需要认证的jwt令牌
                JwtAuthenticationToken jwtToken = new JwtAuthenticationToken(token);
                /*
                 * 通过配置的provider认证器进行token认证
                 * 这里会进入到ProviderManager方法中，并且在该方法中
                 * 会将传入的token对象保证到一个toTest的变量中，会通过该toTest变量
                 * 根据Provider中重写的support方法进行判断与该toTest变量匹配的provider认证器
                 * 这里传入jwtToken，那么与之匹配的就是JwtAuthenticationProvider
                 * 那么将会使用该认证器对该token进行认证
                 *
                 * 因为这里是对登录请求以外的请求做认证，因此传入jwtToken已获得对应的认证器
                 * 而不是传入loginToken
                 * */
                Authentication authentication = this.getAuthenticationManager().authenticate(jwtToken);
                /*
                 * 将认证通过的Authentication对象 填充到Security上下文持有者对象中，
                 * 以支持后续通过自定义的SecurityContextUtil获取认证实体对象（Principal对象）
                 * */
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AuthenticationException e) {
                SecurityContextHolder.clearContext();
                this.getAuthenticationEntryPoint().commence(request, response, e);
                return;
            }
        }
        //过滤传递
        chain.doFilter(request, response);
    }

    protected boolean requiresAuthenticate(HttpServletRequest request){
        for (RequestMatcher requestMatcher : noAuthRequestMatchers){
            if(requestMatcher.matches(request)){
                return false;
            }
        }
        return true;
    }

    private List<RequestMatcher> initMatherList(String...patterns){
        List<RequestMatcher> matchers = new ArrayList<>();
        for(String pattern : patterns){
            matchers.add(new AntPathRequestMatcher(pattern, null));
        }
        return matchers;
    }

}
