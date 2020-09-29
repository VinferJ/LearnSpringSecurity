package me.vinfer.jwtsecurity.constants;

import liquibase.pro.packaged.B;
import lombok.Getter;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    15:56
 * @description
 **/
@Getter
public enum ErrorCode {


    /**认证相关错误*/
    SUCCESS(200,"success"),
    FAILURE(300,"service fail"),
    USER_NOT_FOUND(301,"用户不存在"),
    WRONG_PASSWORD(302,"密码错误"),
    USER_NOT_AVAILABLE(303,"用户已被禁止登录"),
    ACCESS_DENIED(304,"用户权限不足"),
    TOKEN_ERROR(305,"token信息错误"),
    DUPLICATE_LOGIN(306,"该账号已登录，不允许重复登录"),
    TOKEN_EXPIRED(307,"登录信息已过期，请重新登录"),

    /**请求相关的错误*/
    REQUEST_ERROR(401,"请求错误"),
    REQUEST_EXPIRED(402,"请求超时"),

    /**业务逻辑处理相关的错误*/
    BUSINESS_ERROR(501,"业务处理错误"),
    DUPLICATE_USERNAME(502,"用户名已存在");

    /**错误码*/
    private final int code;
    /**错误描述*/
    private final String desc;

    ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
