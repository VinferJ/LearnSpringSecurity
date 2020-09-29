package me.vinfer.jwtsecurity.dao;

import me.vinfer.jwtsecurity.entity.UserRoleDo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-22    15:45
 * @description
 **/
@Repository
@Mapper
public interface UserRoleDao {

    @Select("select id,user_id,role_id from user_role where user_id=#{userId}")
    List<UserRoleDo> queryInfoByUserId(Long userId);

    Integer addUserRole(@Param("userRoleList") List<UserRoleDo> userRoleDoList);

    @Delete("delete from user_role where id=#{id}")
    Integer deleteUserRoleById(Long id);

    Integer updateUserRoleById(UserRoleDo userRoleDo);



}
