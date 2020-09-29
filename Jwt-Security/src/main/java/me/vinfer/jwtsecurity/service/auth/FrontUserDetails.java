package me.vinfer.jwtsecurity.service.auth;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Vinfer
 * @date 2020-09-18    10:36
 * @description security用户验证对象实体类
 **/
@Getter
public class FrontUserDetails extends User{//implements UserDetails {

    public static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Long id;
    private final String username;
    private final String password;
    private final String token;
    /**
     * 该变量设置用户的角色
     * 通过GrantedAuthority控制用户对资源或数据访问权限
     *
     * TODO 继承User用hasRole？直接实现UserDetails用hasAuthority？
     *
     * TODO 目前是只有角色进行访问控制
     *      角色的权限是一个范围较大的集合
     *      尝试将权限控制的粒度做得更细
     *      比如对某些操作即数据的权限：
     *          - FULL:拥有所有读写操作权限
     *          - READ:读取
     *          - UPDATE:更新
     *          - DELETE:删除（高风险操作）
     *          - ADD:新增
     *
     */
    //private final Collection<? extends GrantedAuthority> authorities;

    public FrontUserDetails(Long id, String username, String password, String token, Collection<? extends GrantedAuthority> authorities){
        /*
        * 初始化security的User对象，用于登录验证
        * 前面两个参数对应的是用户名以及登录密码，在实际业务逻辑可以是自定义的登录凭据
        * 比如手机号码和验证码
        * */
        super(username, password, authorities);
        this.id = id;
        this.username = username;
        this.password = password;
        this.token = token;
        //this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        /*
        * 允许用户进行验证，如果为false，则当前用户禁止被授权/认证
        * */
        return true;
    }
}
