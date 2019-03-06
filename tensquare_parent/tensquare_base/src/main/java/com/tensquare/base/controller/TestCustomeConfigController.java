package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试自定义配置的读取和更新
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@RestController
@RequestMapping("/test")
@CrossOrigin
@RefreshScope//用于刷新自定义配置
public class TestCustomeConfigController {

    @Value("${sms.ip}")
    private String ip;

    /**
     * 用于输出id的内容
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/customeConfig")
    public Result customeConfig(){
        System.out.println(ip);
        return new Result(true, StatusCode.OK,"成功",ip);
    }
}
