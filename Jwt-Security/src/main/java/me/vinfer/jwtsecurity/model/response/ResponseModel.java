package me.vinfer.jwtsecurity.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-19    23:15
 * @description
 **/
@Data
@AllArgsConstructor
@ToString
public class ResponseModel {

    private int code;
    private String desc;
    private Object data;

}
