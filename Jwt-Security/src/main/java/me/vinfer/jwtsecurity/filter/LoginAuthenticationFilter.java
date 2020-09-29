package me.vinfer.jwtsecurity.filter;

import me.vinfer.jwtsecurity.model.auth.LoginAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vinfer
 * @date 2020-09-18    14:32
 * @description 登录认证过滤器,对自定义的登录请求路径进行过滤，在认证前进行
 *              参数封装以及AuthenticationDetailsSource对象的创建
 *              如果是账号密码登录，也可以直接使用UsernamePasswordAuthenticationFilter
 *              但是登录路径必须定义为/login，如果要提供自定义的登录路径
 *              可以继承UsernamePasswordAuthenticationFilter，同时提供自定义的
 *              一个公共构造器，带有一个入参：登录路径
 **/
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String SPRING_SECURITY_USER_KEY = "username";
    private static final String SPRING_SECURITY_PASS_KEY = "password";
    private static final String SUPPORT_REQUEST_METHOD_TYPE = "POST";
    private static final boolean POST_ONLY = true;

    /**
     * 过滤器初始化
     * @param loginFilterProcessesUrl     默认的过滤路径（登录路径）
     */
    public LoginAuthenticationFilter(String loginFilterProcessesUrl){
        //通过父类构造器初始化
        super(new AntPathRequestMatcher(loginFilterProcessesUrl,"POST"));
    }

    /*@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        *//*
        * 配置了登录的filter，并且addBefore 在 RequestAuthenticationFilter之前
        * 除登录请求外之所以没有在这里进行过滤操作，是因为没有重写过滤方法
        * 没重写doFilter，那么就不会进入到该filter进行过滤操作
        * 并且登录请求进入的是该filter中的attemptAuthentication方法，
        * 重写了doFilter后就会进入该过滤器中了
        * *//*
        System.out.println("进入了自定义的LoginAuthenticationFilter....");
    }*/

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(POST_ONLY && !request.getMethod().contains(SUPPORT_REQUEST_METHOD_TYPE)){
            /*
            * 如果登录请求不是post请求，抛出认证异常
            * 对登录请求使用post方式进行发送，拒绝其他类型请求，post请求更安全
            * */
            throw new AuthenticationServiceException("Authentication method not support");
        } else {
            /*
            * 登录验证信息获取
            * */
            String username = request.getParameter(SPRING_SECURITY_USER_KEY);
            String password = request.getParameter(SPRING_SECURITY_PASS_KEY);
            if (username == null){
                //重置为空串，防止null类异常
                username = "";
            }
            if(password == null){
                password = "";
            }
            /*去除多余的空格，如果存在*/
            username = username.trim();
            password = password.trim();
            /*
            * 认证前，将验证需要的参数通过自定义的login-token对象封装为一个认证请求对象
            * 该对象会加载到LoginAuthenticationProvider中，因此这里的值传递的顺序
            * 要和provider中取出的对应，这里传入password作为credentials，username作为principal
            * */
            LoginAuthenticationToken authRequest = new LoginAuthenticationToken(password, username);
            authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
            /*
            * 尝试对传递的{@link Authentication}对象进行身份验证，
            * 如果成功，则返回*完全填充的<code> Authentication </ code>对象（包括授予的权限）
            * */
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }
}
