package me.vinfer.jwtsecurity.model.dto;

import lombok.Data;
import lombok.ToString;
import me.vinfer.jwtsecurity.entity.PermissionDo;

import java.util.Set;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-22    15:27
 * @description
 **/
@Data
@ToString
public class RoleDto {

    private final Long id;
    private final String roleName;
    private final String roleDesc;
    private final Set<PermissionDo> permissions;

}
