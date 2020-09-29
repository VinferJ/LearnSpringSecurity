package me.vinfer.jwtsecurity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Vinfer
 * @date 2020-09-23    14:44
 * @description
 **/
@Getter
@AllArgsConstructor
public enum DefaultRoleId {

    /**普通用户*/
    USER(2),

    /*管理员*/
    ADMIN(1);

    private final long id;
}
