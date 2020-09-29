package me.vinfer.jwtsecurity.service.user.impl;

import me.vinfer.jwtsecurity.constants.DefaultRoleId;
import me.vinfer.jwtsecurity.dao.RoleDao;
import me.vinfer.jwtsecurity.dao.UserDao;
import me.vinfer.jwtsecurity.dao.UserRoleDao;
import me.vinfer.jwtsecurity.entity.RoleDo;
import me.vinfer.jwtsecurity.entity.UserDo;
import me.vinfer.jwtsecurity.entity.UserRoleDo;
import me.vinfer.jwtsecurity.exception.BusinessException;
import me.vinfer.jwtsecurity.model.dto.UserDto;
import me.vinfer.jwtsecurity.model.dto.UserRegisterDto;
import me.vinfer.jwtsecurity.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-19    22:56
 * @description
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    public static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Override
    public UserDo queryInfoById(Long userId) {
        return userDao.queryUserInfoById(userId);
    }

    @Override
    public UserDto queryUserById(Long userId) {
        UserDo userDo = userDao.queryUserInfoById(userId);
        Set<RoleDo> userRoles = roleDao.queryRoleByUserId(userId);
        return new UserDto(
                userDo.getId(),
                userDo.getUsername(),
                userDo.getPassword(),
                userRoles
        );
    }

    @Override
    public boolean updatePassWord(Long userId, String newPass) {
        return userDao.updatePass(userId, newPass) == 1;
    }

    @Override
    public boolean delUserById(Long userId) {
        return userDao.deleteUserById(userId) == 1;
    }

    @Override
    public boolean registerAcc(UserDo userDo) {
        UserDo user = new UserDo(null, userDo.getUsername(),ENCODER.encode(userDo.getPassword()));
        return userDao.addUser(user) == 1;
    }

    @Override
    public boolean registerUser(UserRegisterDto registerDto) {
        /*
        * TODO  用户名查重在前端保证
        * */
        //密码加密存储
        registerDto.setPassword(ENCODER.encode(registerDto.getPassword()));
        Set<Long> roles = registerDto.getRoleIds();
        if (roles == null){
            roles = new HashSet<>();
            roles.add(DefaultRoleId.USER.getId());
        }
        UserDo userDo = new UserDo(null, registerDto.getUsername(), registerDto.getPassword());
        //用户表插入
        userDao.addUser(userDo);
        //获取自增id
        Long userId = userDo.getId();
        System.out.println("userId: "+userId);
        if (userId == null){
            throw new BusinessException("用户自增id返回异常");
        }
        List<UserRoleDo> userRoleDoList = new ArrayList<>(roles.size());
        for (Long roleId : roles) {
            userRoleDoList.add(new UserRoleDo(null, userId, roleId));
        }
        System.out.println("roleList: "+userRoleDoList);
        Integer insertTotal = userRoleDao.addUserRole(userRoleDoList);
        if (insertTotal != userRoleDoList.size()){
            throw new BusinessException("用户角色关联新增异常");
        }
        return true;
    }
}
