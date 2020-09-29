package me.vinfer.jwtsecurity.service.auth;

import lombok.AllArgsConstructor;
import me.vinfer.jwtsecurity.dao.RoleDao;
import me.vinfer.jwtsecurity.dao.UserDao;
import me.vinfer.jwtsecurity.entity.RoleDo;
import me.vinfer.jwtsecurity.entity.UserDo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Vinfer
 * @date 2020-09-18    10:47
 * @description 通过该服务类进行dao的登录验证
 **/
@AllArgsConstructor
public class FrontUserDetailsServiceImpl implements UserDetailsService {

    private final UserDao USER_DAO;
    private final RoleDao ROLE_DAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDo user = USER_DAO.queryUserInfoByName(username);
        if(user == null){
            throw new UsernameNotFoundException("用户"+username+"不存在");
        }
        Long userId = user.getId();
        Set<RoleDo> userRoles = ROLE_DAO.queryRoleByUserId(userId);
        /*生成用户的鉴权角色列表，列表中的角色决定了用户对不同资源的访问权限*/
        List<SimpleGrantedAuthority> collect = userRoles.stream()
                .map(RoleDo::getRoleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new FrontUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                "",
                collect);
    }
}
