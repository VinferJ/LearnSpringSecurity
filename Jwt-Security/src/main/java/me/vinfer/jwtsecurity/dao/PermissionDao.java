package me.vinfer.jwtsecurity.dao;

import me.vinfer.jwtsecurity.entity.PermissionDo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Vinfer
 * @date 2020-09-22    15:37
 * @description
 **/
@Repository
@Mapper
public interface PermissionDao {

    Set<PermissionDo> queryPermissionByRoleId(Long roleId);

    @Select("select id,permission_name,permission_url from permission_info")
    List<PermissionDo> queryAllPermissions();

    Integer updatePermissionById(PermissionDo permissionDo);

    @Delete("delete from permission_info where id=#{id}")
    Integer deletePermissionById(Long id);

    Integer addPermission(PermissionDo permissionDo);

}
