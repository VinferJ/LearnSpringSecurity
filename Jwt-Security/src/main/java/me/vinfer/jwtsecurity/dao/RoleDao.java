package me.vinfer.jwtsecurity.dao;

import me.vinfer.jwtsecurity.entity.RoleDo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * @author Vinfer
 * @date 2020-09-22    15:33
 * @description
 **/
@Repository
@Mapper
public interface RoleDao {

    Set<RoleDo> queryRoleByUserId(Long userId);

    @Select("select id,role_name,role_desc from role_info")
    List<RoleDo> queryAllRoles();

    Integer updateRoleById(RoleDo roleDo);

    @Delete("delete from role_info where role_id=#{role_id}")
    Integer deleteRoleById(Long roleId);

    Integer addRole(RoleDo roleDo);


}
