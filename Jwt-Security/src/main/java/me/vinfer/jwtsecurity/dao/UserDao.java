package me.vinfer.jwtsecurity.dao;

import me.vinfer.jwtsecurity.entity.UserDo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    10:49
 * @description
 **/
@Repository
@Mapper
public interface UserDao {

    /**
     * 通过用户id查询用户
     * @param username  用户id
     * @return          返回用户实体信息
     */
    @Select("select id,username,password from user_info where username=#{username}")
    UserDo queryUserInfoByName(String username);

    @Select("select username from user_info where username=#{username}")
    String queryUsername(String username);

    /**
     * 通过用户id查询用户信息
     * @param id        用户id
     * @return          用户信息
     */
    @Select("select id,username,password from user_info where id=#{id}")
    UserDo queryUserInfoById(Long id);

    /**
     * 修改密码
     * @param userId        用户id
     * @param newPass       新的密码
     * @return              表更新行数
     */
    @Update("update user_info set password=#{newPass} where id=#{userId}")
    Integer updatePass(@Param("userId") Long userId, @Param("newPass") String newPass);

    /**
     * 删除用户
     * @param id        用户id
     * @return          表影响行数
     */
    @Delete("delete from user_info where id=#{id}")
    Integer deleteUserById(Long id);

    /**
     * 新增用户
     * @param userDo        用户实体类
     * @return                  表影响行数
     */
    Long addUser(UserDo userDo);

}
