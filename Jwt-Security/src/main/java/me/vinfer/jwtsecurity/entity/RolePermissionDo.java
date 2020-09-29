package me.vinfer.jwtsecurity.entity;


import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-22    15:47
 * @description
 **/
@Data
@ToString
public class RolePermissionDo {

    private final Long id;
    private final Long roleId;
    private final Long permissionId;


}
