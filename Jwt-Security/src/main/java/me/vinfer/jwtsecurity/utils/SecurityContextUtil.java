package me.vinfer.jwtsecurity.utils;

import me.vinfer.jwtsecurity.service.auth.FrontUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    18:01
 * @description
 **/
public abstract class SecurityContextUtil {

    /**
     * 获取已被授权认证的用户的用户信息
     *
     * SecurityContext默认是使用ThreadLocal保存的，可以通过SecurityContextHolder的initialize方法查看
     * 该Holder对象初始化时是使用MODEL_THREADLOACL来作为SecurityContextHolderStrategy的
     * 这意味着每个登录请求认证的用户都有自己单独的SecurityContext，因此SecurityContext中的user信息
     * 与认证过的请求的用户信息是一一对应的，
     * 因此请求中需要获取用户信息时可以直接通过SecurityContext获取，不需要查数据库或者让前端带上用户数据
     *
     * TODO     看ThreadLocal的原理
     *
     * @return  {@link FrontUserDetails}
     */
    public static FrontUserDetails user(){
        return (FrontUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
