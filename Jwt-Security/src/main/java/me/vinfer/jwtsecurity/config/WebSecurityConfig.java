package me.vinfer.jwtsecurity.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.vinfer.jwtsecurity.dao.RoleDao;
import me.vinfer.jwtsecurity.dao.UserDao;
import me.vinfer.jwtsecurity.filter.RequestAuthenticationFilter;
import me.vinfer.jwtsecurity.filter.LoginAuthenticationFilter;
import me.vinfer.jwtsecurity.handler.*;
import me.vinfer.jwtsecurity.model.auth.FrontAuthenticationDetailsSource;
import me.vinfer.jwtsecurity.provider.CacheProvider;
import me.vinfer.jwtsecurity.provider.JwtAuthenticationProvider;
import me.vinfer.jwtsecurity.provider.LoginAuthenticationProvider;
import me.vinfer.jwtsecurity.service.auth.FrontUserDetailsServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author Vinfer
 * @date 2020-09-18    10:33
 * @description security核心配置
 *              {@link EnableGlobalMethodSecurity}该注解配置开启全局方法的权限控制
 *              即可使用@PreAuthorize注解进行方法级的鉴权配置
 **/
@Configuration
@ConfigurationProperties(prefix = "security")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Setter
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {

    /**
     * 配置自己的需要忽略认证的url
     * 一般是一些web静态资源、swagger-doc等访问路径
     */
    private static final String[] DEFAULT_WEB_IGNORING_LIST = {};

    /**
     * 配置认证白名单，一般是注册、验证码发送等服务的访问路径
     */
    private static final String[] DEFAULT_AUTH_WHITELIST = {
            //登录请求的url也需要放行
            "/login",
            "/no-auth/**",
            "/register"
    };

    private String loginUrl;
    private String logoutUrl;
    private String[] webIgnoringList;
    private String[] authWhiteList;


    @Override
    public void afterPropertiesSet() throws Exception {
        /*
        * 未配置登入登出以及两个认证白名单时使用默认配置
        * */
        if (webIgnoringList == null || webIgnoringList.length == 0){
            webIgnoringList = DEFAULT_WEB_IGNORING_LIST;
        }
        if (authWhiteList == null || authWhiteList.length == 0){
            authWhiteList = DEFAULT_AUTH_WHITELIST;
        }
    }


    /**
     * 缓存管理使用单例，进行本地全局缓存
     * @return      缓存管理提供者对象
     */
    CacheProvider cacheProvider(){
        return this.getApplicationContext().getBean(CacheProvider.class);
    }


    /**
     * 配置用户信息服务类
     * @return      返回用户信息服务实现类
     */
    @Bean
    FrontUserDetailsServiceImpl frontUserDetailsService(){
        UserDao userDao = this.getApplicationContext().getBean(UserDao.class);
        RoleDao roleDao = this.getApplicationContext().getBean(RoleDao.class);
        return new FrontUserDetailsServiceImpl(userDao,roleDao);
    }

    @Bean
    AuthSuccessHandler authSuccessHandler(){
        return new AuthSuccessHandler(cacheProvider());
    }

    @Bean
    AuthFailureHandler authFailureHandler(){
        return new AuthFailureHandler();
    }

    /*
    * TODO  匿名认证配置
    * */

    @Bean
    AnonymousAuthenticationProvider anonymousAuthenticationProvider(){
        return new AnonymousAuthenticationProvider("FILTER_KEY_PROVIDER_KEY");
    }

    AnonymousAuthenticationFilter anonymousAuthenticationFilter(){
        return new AnonymousAuthenticationFilter("FILTER_KEY");
    }


    @Bean
    LoginAuthenticationProvider loginAuthenticationProvider(){
        return new LoginAuthenticationProvider(userDetailsService());
    }

    @Bean
    LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter(loginUrl);
        /*
        * 配置认证管理对象，因为这里需要进行Bean装配，因此要通过authenticationManagerBean()方法获取
        * 其获得的authenticationManager对象所装载的provider与下面通过authenticationManager()获取到的对象
        * 所装载的provider是一样的
        * */
        filter.setAuthenticationManager(authenticationManagerBean());
        //配置自定义的认证成功时的handler
        filter.setAuthenticationSuccessHandler(authSuccessHandler());
        //配置自定义的认证失败时的handler
        filter.setAuthenticationFailureHandler(authFailureHandler());
        //配置并提供一个detailSource，所有的认证都将基于该对象
        filter.setAuthenticationDetailsSource(authenticationDetailsSource());
        return filter;
    }

    @Bean
    UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        String defaultLoginUrl = "/login";
        if (!loginUrl.equals(defaultLoginUrl)){
            log.warn("your login request url is not equal with '/login', " +
                    "spring-security will be unable to authorize your login request if you load this filter into filter chain");
        }
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        //配置自定义的认证成功时的handler
        filter.setAuthenticationSuccessHandler(authSuccessHandler());
        //配置自定义的认证失败时的handler
        filter.setAuthenticationFailureHandler(authFailureHandler());
        //配置并提供一个detailSource，所有的认证都将基于该对象
        filter.setAuthenticationDetailsSource(authenticationDetailsSource());
        return filter;
    }

    @Bean
    UnauthorizedEntryPoint authenticationEntryPoint(){
        return new UnauthorizedEntryPoint(cacheProvider());
    }

    @Bean
    FrontAuthenticationDetailsSource authenticationDetailsSource(){
        return new FrontAuthenticationDetailsSource();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        /*配置密码加密器*/
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证器管理对象的配置/创建，一般在这里配置自定义的认证器或服务
     * @param auth          认证管理器的builder
     * @throws Exception    方法异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //配置用户认证服务，实现通过dao认证用户
                .userDetailsService(frontUserDetailsService())
                //配置密码加密器
                .passwordEncoder(passwordEncoder())
                .and()
                //匿名认证支持
                .authenticationProvider(anonymousAuthenticationProvider())
                //配置自定义的登录认证器，使用单例
                .authenticationProvider(loginAuthenticationProvider())
                //配置自定义的请求认证器,jwt的使用多例
                .authenticationProvider(new JwtAuthenticationProvider(userDetailsService(),cacheProvider()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /*
                * session管理的配置，这里使用token验证，不需要创建session
                * STATELESS:
                *   Spring Security永远不会创建{@link Http Session}，并且永远不会使用它来获取{@link SecurityContext}
                * */
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //禁用csrf
                .cors().and().csrf().disable()
                //表单登录
                .formLogin().loginProcessingUrl("/auth/login").permitAll().and()
                .logout()
                .logoutUrl(logoutUrl)
                //配置自定义的登出成功的handler
                .logoutSuccessHandler(new FrontLogoutSuccessHandler(cacheProvider()))
                .and()
                //访问的请求需要进行授权访问
                .authorizeRequests()
                //对访问白名单的请求放行
                .antMatchers(authWhiteList).permitAll()
                //.antMatchers("/user/**").hasAnyAuthority("user","admin")
                //.antMatchers("/admin/**").hasAuthority("admin")
                .anyRequest().authenticated()
                .and()
                //自定义一个前置filter，在进入登录认证前再进行一些数据处理，这里需要使用多例
                //.addFilterBefore(new RequestBodyDecryptFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //向filter传入认证白名单，跳过授权认证，每个请求都需要有自己的认证过滤，因此也需要使用多例
                .addFilter(new RequestAuthenticationFilter(authenticationManager(), authenticationEntryPoint(),authWhiteList))
                .exceptionHandling()
                //认证入口点
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(new FrontAccessDeniedHandler());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(webIgnoringList);
    }


}
