package me.vinfer.simplesecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    10:14
 * @description
 **/
@RestController
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "welcome!";
    }

}
