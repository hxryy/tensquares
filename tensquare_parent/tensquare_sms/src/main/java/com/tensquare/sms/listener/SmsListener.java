package com.tensquare.sms.listener;

import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用于监听消息队列中的消息，当监听到后，消费消息
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;

    /**
     * 实际消费消息的方法
     */
    @RabbitHandler
    public void sendsms(Map<String,String> map)throws Exception{
        String mobile = map.get("mobile");
        String code = map.get("code");
        //调用工具类实现验证码的发送
        smsUtil.sendSms(mobile,template_code,sign_name,"{\"code\":\""+code+"\"}");
        System.out.println("接收验证码的手机号是："+mobile+",验证码是："+code);
    }
}
