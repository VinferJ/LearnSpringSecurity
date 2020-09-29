package me.vinfer.jwtsecurity.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author Vinfer
 * @date 2020-09-20    20:48
 * @description 请求体加密配置类
 **/
@Configuration
@ConfigurationProperties(prefix = "encrypt.body")
@Data
public class EncryptBodyConfig implements InitializingBean {

    /**解密处理开关*/
    private Boolean enable;
    /**请求有效时间/超时时间，单位为秒*/
    private Long requestValidTime;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(enable == null){
            enable = false;
        }
        if (enable){
            /*设置默认值为10s*/
            if(getRequestValidTime() == null){
                requestValidTime = 10L;
            }
        }
    }




}
