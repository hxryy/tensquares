package com.tensquare.manager;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理员网关
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Component
public class ManagerZuulFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 核心方法：
     *   此处我们要验证管理员消息头。
     *   管理员的权限和用户的权限区别。
     *   用户的操作：
     *      有可能需要用户权限，也有可能不需要
     *      例如：
     *          查询文章，查看问题都不需要权限
     *          发布文章，发布问题都需要用户权限
     *   管理员的操作：
     *      无论何时，都必须具备管理员权限，否则就是无权访问。
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //1.获取请求上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //2.获取request
        HttpServletRequest request = requestContext.getRequest();

        //特殊情况之：管理员登录操作,此时是不可能有管理员权限消息头的，应该直接放行
        String url = request.getRequestURL().toString();
        if(url.indexOf("/admin/login") > 0){
            //当前请求是管理员操作，需要直接放行
            System.out.println("管理员登录操作！");
            return null;
        }

        //特殊情况之：跨域访问。跨域访问都是两次请求。第一次称为预请求，第二次称为正式请求
        //在预请求时，不携带任何请求信息。所以不可能有管理员权限消息头，应该直接放行。
        String method = request.getMethod();
        if("OPTIONS".equalsIgnoreCase(method)){
            System.out.println("跨域访问的预请求！");
            return null;
        }


      //3.获取请求消息头
        String header = request.getHeader("Authorization");
        //4.判断是否有此消息头，如果没有的话就不用继续执行了
        if(!StringUtils.isEmpty(header)){
            //5.把消息头转发
            requestContext.addZuulRequestHeader("Authorization",header);
            //6.放行
            return null;
        }
        //7.没有消息头，就不用继续执行，而是从此处直接响应了
        requestContext.setSendZuulResponse(false);//取值为false的时候，表示终止执行，无论后面的返回值是什么，都不再继续了
        requestContext.setResponseStatusCode(401);//设置响应状态码
        requestContext.getResponse().setContentType("application/json;charset=UTF-8");//设置响应内容的MIME类型和字符集
        requestContext.setResponseBody("{\"flag\": false,\"code\": 20003,\"message\": \"权限不足\"}");
        return null;
    }
}
