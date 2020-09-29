package me.vinfer.jwtsecurity.service.user;

import me.vinfer.jwtsecurity.entity.UserDo;
import me.vinfer.jwtsecurity.model.dto.UserDto;
import me.vinfer.jwtsecurity.model.dto.UserRegisterDto;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vinfer
 * @date 2020-09-19    22:52
 * @description
 **/
public interface UserService {

    /**
     * 根据用户id查询用户信息
     * @param userId        用户id
     * @return              返回用户信息
     */
    @Deprecated
    UserDo queryInfoById(Long userId);

    /**
     * 根据用户id查询用户信息
     * @param userId        用户id
     * @return              返回用户信息
     */
    UserDto queryUserById(Long userId);

    /**
     * 更改密码
     * @param userId        用户id
     * @param newPass       新密码
     * @return              更改结果
     */
    boolean updatePassWord(Long userId,String newPass);

    /**
     * 删除用户
     * @param userId        用户id
     * @return              删除结果
     */
    boolean delUserById(Long userId);

    /**
     * 注册用户
     * @param userDo    用户实体
     * @return              注册结果
     */
    @Deprecated
    boolean registerAcc(UserDo userDo);

    /**
     * 注册用户，赋予默认指定角色，如果roles是null
     * 则指定默认角色为user
     * @param registerDto       用户数据传输对象
     * @return                  注册结果
     */
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    boolean registerUser(UserRegisterDto registerDto);

}
