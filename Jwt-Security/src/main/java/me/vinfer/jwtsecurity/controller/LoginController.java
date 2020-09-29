package me.vinfer.jwtsecurity.controller;

import javafx.scene.chart.ValueAxis;
import me.vinfer.jwtsecurity.constants.ErrorCode;
import me.vinfer.jwtsecurity.entity.UserDo;
import me.vinfer.jwtsecurity.service.user.UserService;
import me.vinfer.jwtsecurity.utils.ResponseUtil;
import me.vinfer.jwtsecurity.utils.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    16:58
 * @description
 **/
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth/login")
    public Object login(@RequestBody UserDo userDo){
        System.out.println("attempt to login....");
        System.out.println(userDo);
        return null;
    }


    /*
    * hasRole与hasAuthority使用的区别
    * 使用hasRole时，Authority的元素必须包含'ROLE_'前置，也就是数据库里存储的
    * 角色名称必须加上ROLE_,如：ROLE_USER，而在Controller使用时则是加前缀不加前缀都可以
    * 使用hasAuthority时，则不需要配置ROLE_前缀，并且Controller中配置的值必须和数据库
    * 存储的角色名称一样，比如数据库存储的是：'user'，那么Controller中配置就必须是'user'
    * */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/page")
    public Object adminPage(){
        System.out.println("currentUser: ["+ SecurityContextUtil.user().getUsername()+"], holding roles:"
                +SecurityContextUtil.user().getAuthorities());
        return "admin page info...";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping(value = "/user/page")
    public Object userPage(){
        /*
        * 认证后的用户信息是线程私有的，在ThreadLocal保存的SecurityContext对象中
        * */
        System.out.println("currentUser: ["+ SecurityContextUtil.user().getUsername()+"], holding roles:"
                +SecurityContextUtil.user().getAuthorities());
        return "user page info...";
    }


    @PostMapping("/no-auth/page")
    public String noAuthPage(@RequestBody UserDo userDo){
        System.out.println(userDo);
        return userDo.toString();
    }

    @PostMapping("/register")
    public Object registerUser(@RequestBody UserDo userDo){
        boolean success = userService.registerAcc(userDo);
        return success? ResponseUtil.emptySuccess():ResponseUtil.error(ErrorCode.FAILURE);
    }

    @GetMapping("/any")
    public String anonymousPage(){
        return "any";
    }


}
