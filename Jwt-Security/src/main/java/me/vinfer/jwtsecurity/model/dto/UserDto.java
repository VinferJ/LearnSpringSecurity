package me.vinfer.jwtsecurity.model.dto;

import lombok.*;
import me.vinfer.jwtsecurity.entity.RoleDo;

import java.util.Set;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-22    15:26
 * @description
 **/
@Data
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private Set<RoleDo> roles;

}
