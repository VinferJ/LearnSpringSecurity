package me.vinfer.jwtsecurity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    15:31
 * @description
 **/
@Getter
@AllArgsConstructor
public enum TimeConfigEnum {

    /**token过期时间，单位是s，默认为15天*/
    TOKEN_EXPIRATION_TIME(60 * 60 * 24 * 15);

    /**时间数值*/
    private final long time;

}
