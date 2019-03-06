package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用于和基础微服务建立关联的接口，用于定义要访问基础微服务的方法
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 *
 * 注意事项有2：
 *   第一个：RequestMapping注解的value属性，别忘了把Controller类上的RequestMapping配置拿过来
 *   第二个：无论value属性的占位符名称和方法形参名称是否一致，PathVariable注解上的value属性都必须写，写{}中占位符的名称
 */
@FeignClient(value="tensquare-base",fallback = LabelClientImpl.class)
public interface LabelClient {

    @RequestMapping(method = RequestMethod.GET,value = "/label/{id}")
    Result findById(@PathVariable("id") String id);
}
