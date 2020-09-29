package me.vinfer.jwtsecurity.provider;

import io.jsonwebtoken.ExpiredJwtException;
import liquibase.pro.packaged.E;
import lombok.extern.slf4j.Slf4j;
import me.vinfer.jwtsecurity.exception.TokenExpiredException;
import me.vinfer.jwtsecurity.model.auth.JwtAuthenticationToken;
import me.vinfer.jwtsecurity.service.auth.FrontUserDetails;
import me.vinfer.jwtsecurity.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author Vinfer
 * @date 2020-09-18    11:53
 * @description 对除登录请求外的所有需要授权认证的请求进行认证
 **/
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final CacheProvider cacheProvider;

    public JwtAuthenticationProvider(UserDetailsService userDetailsService,CacheProvider cacheProvider){
        this.userDetailsService = userDetailsService;
        this.cacheProvider = cacheProvider;
    }


    /**
     * 用户认证
     * @param authentication        认证器对象
     * @return                      认证成功时，返回认证token信息
     * @throws AuthenticationException      认证异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) authentication;
        String token = (String) jwt.getCredentials();
        UserDetails user;
        user = this.retrieveUser(token, jwt);
        if(!user.isEnabled()){
            log.info("用户[{}]被禁止访问，本次访问已拦截",user.getUsername());
            throw new DisabledException("用户["+user.getUsername()+"]已被禁止");
        }

        /*
        * token过期验证
        * 如果已经过期，那么需要用户重新登录
        * */
        validTokenExpiredTime(token);


        //生成认证成功的token对象
        UsernamePasswordAuthenticationToken successToken =
                new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(),user.getAuthorities());
        //存储验证请求信息
        successToken.setDetails(authentication.getDetails());
        //返回认证授权的token信息
        return successToken;
    }

    /**
     * 认证支持，这里根据自定的Token对象来判断是否与
     * 传入的认证器是同一接口或者拥有同一个基类/父类来判断是否提供
     * 该类所配置的认证支持
     * 也就是根据该方法来判断，是否要使用该类作为授权认证的provider
     * @param authentication    认证器对象
     * @return      是否支持对提供的实例认证
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 用户信息拉取
     * 在用户登录验证通过时会生成token并存放到cache中
     * 有新请求时会进入该请求认证阶段，认证通过会封装用户信息并返回
     * @param token     token令牌
     * @param jwt       jwt对象
     * @return          返回security封装的用户信息对象
     * @throws BadCredentialsException      认证异常
     */
    protected final UserDetails retrieveUser(String token, JwtAuthenticationToken jwt) throws BadCredentialsException{
        try {
            @SuppressWarnings("unchecked")
            Map<String,Object> loginUser = (Map<String, Object>) cacheProvider.getLoginUser(token);
            if(loginUser == null){
                throw new BadCredentialsException("错误的token！");
            }
            FrontUserDetails userDetails = (FrontUserDetails) userDetailsService.loadUserByUsername((String) loginUser.get("username"));

            return new FrontUserDetails(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    token,
                    userDetails.getAuthorities()
            );
        }catch (BadCredentialsException e){
            log.info("未登录的请求访问，已成功拦截");
            throw e;
        }
    }

    private void validTokenExpiredTime(String token)throws TokenExpiredException{
        JwtUtil.validToken(token);
    }

}
