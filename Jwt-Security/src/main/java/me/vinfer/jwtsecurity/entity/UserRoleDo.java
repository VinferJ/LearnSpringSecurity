package me.vinfer.jwtsecurity.entity;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-22    15:46
 * @description
 **/
@Data
@ToString
public class UserRoleDo {

    private final Long id;
    private final Long userId;
    private final Long roleId;

}
