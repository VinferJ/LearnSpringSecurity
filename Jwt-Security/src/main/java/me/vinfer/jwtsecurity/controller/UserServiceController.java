package me.vinfer.jwtsecurity.controller;

import me.vinfer.jwtsecurity.constants.ErrorCode;
import me.vinfer.jwtsecurity.model.dto.UserRegisterDto;
import me.vinfer.jwtsecurity.service.user.UserService;
import me.vinfer.jwtsecurity.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-23    15:28
 * @description
 **/
@RestController
@RequestMapping("/user")
public class UserServiceController {


    @Autowired
    UserService userService;

    @PostMapping(value = "/register")
    public Object userRegister(@RequestBody UserRegisterDto registerDto){
        boolean success = userService.registerUser(registerDto);
        return success?ResponseUtil.emptySuccess():ResponseUtil.error(ErrorCode.FAILURE);
    }




}
