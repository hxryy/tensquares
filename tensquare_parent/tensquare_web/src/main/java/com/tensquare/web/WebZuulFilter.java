package com.tensquare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/** 前台网关过滤器
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Component
public class WebZuulFilter extends ZuulFilter {
    /**
     * 指定当前过滤器的执行时机
     * pre
     * route
     * error
     * post
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 指定过滤器的执行顺序，当有多个网关过滤器时，用于确定执行顺序的。
     * 数值越小，执行时间点越早。
     * 取值是0 和 正整数
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 指定当前过滤器是否执行
     * 取值为true的时候表示执行。
     * 为false的时候不执行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 指定核心业务代码
     * 我们要在这里实现消息头的获取和转发
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //1.获取请求上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //2.获取request
        HttpServletRequest request = requestContext.getRequest();
        //3.获取请求消息头
        String header = request.getHeader("Authorization");
        //4.实现消息头的转发
        requestContext.addZuulRequestHeader("Authorization",header);
        //5.过滤器放行
        return null;//当返回值是null的时候，表示放行
    }
}
