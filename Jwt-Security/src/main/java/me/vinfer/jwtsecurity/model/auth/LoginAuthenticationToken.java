package me.vinfer.jwtsecurity.model.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @author Vinfer
 * @date 2020-09-18    14:49
 * @description 如果是账号密码的登录形式，可以直接使用UsernamePasswordAuthenticationToken类作为token封装
 *              这里编写的token类与它也是完全一致的
 **/
public class LoginAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private Object credentials;
    private Object principal;

    /**
     * 认证前的初始化
     * @param credentials   证书/令牌
     * @param principal     认证实体（被认证的用户对象）
     */
    public LoginAuthenticationToken(Object credentials,Object principal){
        super(null);
        this.credentials = credentials;
        this.principal = principal;
        //设置为未认证
        this.setAuthenticated(false);
    }

    /**
     * 认证通过后的初始化
     * @param credentials   证书/令牌
     * @param principal     被认证实体
     * @param authorities   已授权对象集合
     */
    public LoginAuthenticationToken(Object credentials, Object principal, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.credentials = credentials;
        this.principal = principal;
        //设置为已认证
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        /*
        * 禁止通过setter设置认证状态，必须重写该方法
        * 否则父类方法会支持这种行为
        * */
        if(isAuthenticated()){
            throw new IllegalArgumentException("Unable to set authentication by setter method，" +
                    "please use constructor which takes GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    /**
     * 擦除证书，登出时使用
     */
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
