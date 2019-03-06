package com.tensquare.qa.interceptors;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器，用于判断是否有权限（包含管理员和用户）
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 在控制器方法执行之前，先执行
     * @param request
     * @param response
     * @param handler
     * @return  当返回值是true的时候表示放行。false表示不放行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求消息头
        String header = request.getHeader("Authorization");
        //2.判断是否有此消息头，同时还要判断是否以Bearer开头
        if(!StringUtils.isEmpty(header) && header.startsWith("Bearer ")){
            //3.获取token
            String token = header.split(" ")[1];
            //4.解析token
            Claims claims = jwtUtil.parseJWT(token);
            //5.如果claims不为null的话，验证是管理员还是用户
            if(claims != null){
                String roles = (String)claims.get("roles");
                if("admin_role".equals(roles)){
                    //管理员
                    request.setAttribute("admin_claims",claims);
                }else if("user_role".equals(roles)){
                    //普通用户
                    request.setAttribute("user_claims",claims);
                }else {
                    //其他权限
                }
            }
        }
        return true;//拦截器无论什么情况都放行
    }
}
