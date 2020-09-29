package me.vinfer.jwtsecurity.provider;

import lombok.AllArgsConstructor;
import me.vinfer.jwtsecurity.model.auth.LoginAuthenticationToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * @author Vinfer
 * @date 2020-09-18    16:11
 * @description (仅)对登录请求做授权认证
 *              如果业务没有特殊的逻辑处理需求，也可以直接使用DaoAuthenticationProvider来进行登录请求的授权认证
 *              使用该provider，数据库加载到的密码需要是被配置的同类的密码加密器所加密
 *              这里的passwordEncoder默认配置是bcrypt，也就需要数据库存储的密码是被该加密器锁加密
 *              如果使用其他类型加密密码，就需要在WebSecurityConfig中配置对应的passwordEncoder
 **/
@AllArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private final UserDetailsService userDetailsService;
    private static final PasswordEncoder PASS_HELPER = new BCryptPasswordEncoder();

    /**
     * 做属性检查
     * @throws Exception    异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //userDetails服务为null时中断装载
        Assert.notNull(userDetailsService, "A userDetailsService must be set");
    }

    /**
     * 开始进行登录请求的授权认证
     * @param authentication            待完成认证的认证对象
     * @return                          认证成功时返回完全填充的认证对象
     * @throws AuthenticationException  认证异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginAuthenticationToken token = (LoginAuthenticationToken) authentication;
        /*
        * 取出的属性与认证前的封装放入时要一致
        * 这里的用户名和密码即登录时所输入的
        * */
        String username = (String) token.getPrincipal();
        String password = (String) token.getCredentials();
        UserDetails user;
        user = this.retrieveUser(username, token);
        //用户的真正密码，从数据库读取并且已经过encoder加密
        String encodePass = user.getPassword();
        /*
        *
        * 验证登录密码
        * 数据库加密存储密码，前端AES加密传输
        * 进入后端时会被解密成明文
        * BCryptPasswordEncoder对密码匹配：
        *   该加密器每次加密时的hash值都不一样，因此每次都会生成不一样的密码
        *   但是匹配时只要传入的密码是初始的明文密码那么就可以正确匹配
        * */
        if (!PASS_HELPER.matches(password, encodePass)){
            throw new BadCredentialsException("登录密码错误");
        }
        if (!user.isEnabled()){
            throw new DisabledException("用户"+username+"已被禁止登录");
        }
        LoginAuthenticationToken successToken = new LoginAuthenticationToken(authentication.getCredentials(),user,user.getAuthorities());
        successToken.setDetails(authentication.getDetails());
        return successToken;
    }

    protected final UserDetails retrieveUser(String username, LoginAuthenticationToken authentication) throws UsernameNotFoundException{
        /*
        * 该方法中已经不需要再try-catch捕获UsernameNotFound等异常
        * 因为在userDetails为空时并抛出InternalAuthenticationServiceException时，
        * 该异常会被ProviderManager所捕获，并且会转换成相应的认证失败的异常类型
        * 然后统一在onAuthenticationFailure中处理
        * 只要idea提示了该异常已被重复抛出时，就不需要在try-catch该异常了
        * */
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails == null){
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        }else {
            return userDetails;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
