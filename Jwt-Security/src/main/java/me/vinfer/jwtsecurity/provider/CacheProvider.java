package me.vinfer.jwtsecurity.provider;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    11:46
 * @description 用于缓存用户token信息，此处只做简单的本地缓存，可以替换成redis做全局缓存
 **/
@Component
public class CacheProvider {

    /**
     * 使用认证通过的token作为key值，保存登录用户的信息
     * value是一个map，包含用户id、username、password属性
     * */
    private static final ConcurrentHashMap<String,Object> LOGIN_USER_CACHE = new ConcurrentHashMap<>();

    /**
     * 使用已登录的用户的用户名作为key值，保存用户登录状态，
     * value为一个map，包含state属性以及token属性
     * state：1表示已登录，0表示登出或未登录
     * token：用户登录时所保存的token信息
     * */
    public static final ConcurrentHashMap<String,Object> LOGIN_STATE_CACHE = new ConcurrentHashMap<>();

    public void storeLoginUser(String k, Object v){
        LOGIN_USER_CACHE.put(k, v);
    }

    public void storeLoginState(String k,Object v){
        LOGIN_STATE_CACHE.put(k, v);
    }

    public void removeLoginUser(String k){
        LOGIN_USER_CACHE.remove(k);
    }

    public void removeLoginState(String k){
        LOGIN_STATE_CACHE.remove(k);
    }

    public void updateLoginUser(String k,Object v){
        LOGIN_USER_CACHE.replace(k, v);
    }

    public void updateLoginState(String k,Object v){
        LOGIN_STATE_CACHE.replace(k, v);
    }

    public Object getLoginUser(String k){
        return LOGIN_USER_CACHE.get(k);
    }

    public Object getLoginState(String k){
        return LOGIN_STATE_CACHE.get(k);
    }

}
