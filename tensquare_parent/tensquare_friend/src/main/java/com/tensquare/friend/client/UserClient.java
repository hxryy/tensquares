package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用于调用用户微服务的接口
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@FeignClient(value="tensquare-user")
public interface UserClient {


    /**
     * 更新用户粉丝数
     * @param userid
     * @param fans
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/user/incfans/{userid}/{fans}")
    void updateFans(@PathVariable("userid") String userid, @PathVariable("fans") int fans);


    /**
     * 更新用户关注数
     * @param userid
     * @param follow
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/user/incfollow/{userid}/{follow}")
    void updateFollow(@PathVariable("userid") String userid,@PathVariable("follow") int follow);
}
