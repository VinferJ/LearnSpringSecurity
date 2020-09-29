package me.vinfer.jwtsecurity.entity;

import lombok.*;

import java.util.Set;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-22    14:30
 * @description
 **/
@Data
@ToString
public class RoleDo {

    private final Long id;
    private final String roleName;
    private final String roleDesc;

}
