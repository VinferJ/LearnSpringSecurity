package me.vinfer.jwtsecurity.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    10:50
 * @description
 **/
@Data
@ToString
public class UserDo {

    private final Long id;
    private final String username;
    private final String password;


}
