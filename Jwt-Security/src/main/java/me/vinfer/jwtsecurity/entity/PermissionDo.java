package me.vinfer.jwtsecurity.entity;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-22    14:33
 * @description
 **/
@Data
@ToString
public class PermissionDo {

    private final Long id;
    private final String permissionName;
    private final String permissionUrl;

}
