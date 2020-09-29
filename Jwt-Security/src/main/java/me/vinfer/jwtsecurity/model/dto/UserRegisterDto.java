package me.vinfer.jwtsecurity.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-23    14:39
 * @description
 **/
@ToString
@Data
public class UserRegisterDto{

    private String username;
    private String password;
    private Set<Long> roleIds;

}
