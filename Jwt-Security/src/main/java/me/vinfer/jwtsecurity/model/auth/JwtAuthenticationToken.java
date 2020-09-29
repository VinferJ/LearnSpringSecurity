package me.vinfer.jwtsecurity.model.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * @author Vinfer
 * @date 2020-09-18    11:35
 * @description     自定义的jwt认证token对象
 **/
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * 证书/令牌对象，即token信息
     */
    private Object credentials;

    public JwtAuthenticationToken(Object credentials) {
        super(null);
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        /*Principal：被认证后的认证主体，这里没有涉及到该对象，所以用默认重新即可*/
        return null;
    }
}
