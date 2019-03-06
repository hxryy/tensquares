package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 交友的控制器
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@RestController
@RequestMapping("/friend")
@CrossOrigin
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 添加好友或者非好友
     * @param friendid
     * @param type  当type是1的时候，是添加好友。当type是0的时候，是拉黑（添加非好有）
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/like/{friendid}/{type}")
    public Result addFriend(@PathVariable("friendid") String friendid, @PathVariable("type") String type, HttpServletRequest request){
        //1.从请求域中获取当前用户的用户权限
        Claims claims = (Claims)request.getAttribute("user_claims");
        //2.判断是否有权限
        if(claims == null){
            return new Result(false,StatusCode.ACCESSERROR,"无权访问");
        }
        //3.取出当前claims中的用户id
        String userid = claims.getId();
        //4.判断是添加好友还是添加非好友
        if("1".equals(type)) {
            //添加好友
            friendService.addFriend(userid, friendid);
        }else{
            //添加非好友
            friendService.addNoFriend(userid,friendid);
        }
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 删除好友
     * @param friendid
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE,value = "/{friendid}")
    public Result deleteFriend(@PathVariable("friendid") String friendid,HttpServletRequest request){
        //1.从请求域中获取当前用户的用户权限
        Claims claims = (Claims)request.getAttribute("user_claims");
        //2.判断是否有权限
        if(claims == null){
            return new Result(false,StatusCode.ACCESSERROR,"无权访问");
        }
        //3.取出当前claims中的用户id
        String userid = claims.getId();
        //4.调用业务层删除
        friendService.deleteFriend(userid,friendid);
        //5.返回
        return new Result(true,StatusCode.OK,"删除成功！");
    }










}
