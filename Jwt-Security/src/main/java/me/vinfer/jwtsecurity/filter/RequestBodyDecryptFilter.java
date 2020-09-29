package me.vinfer.jwtsecurity.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-21    14:54
 * @description OncePerRequestFilter：过滤器基类，该过滤器拥有比RequestBodyAdviceAdapter更高的执行权限
 *              会优先执行该基类的过滤器
 *
 **/
public class RequestBodyDecryptFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String password = request.getParameter("password");
        if (password != null){
            password = Base64.getEncoder().encodeToString(password.getBytes());
            request.setAttribute("password", password);
            System.out.println(password);
        }
        filterChain.doFilter(request, response);
    }


}
