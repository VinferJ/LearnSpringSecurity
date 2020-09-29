package me.vinfer.jwtsecurity.dao;

import me.vinfer.jwtsecurity.entity.RolePermissionDo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-22    15:52
 * @description
 **/
@Repository
@Mapper
public interface RolePermissionDao {

    @Select("select id,role_id,permission_id from role_permission where role_id=#{roleId}")
    List<RolePermissionDo> queryInfoByRoleId(Long roleId);

    Integer addRolePermission(RolePermissionDo rolePermissionDo);

    @Delete("delete from role_permission where id={id}")
    Integer deleteRolePermissionById(Long id);

    Integer updateRolePermissionById(RolePermissionDo rolePermissionDo);

}
