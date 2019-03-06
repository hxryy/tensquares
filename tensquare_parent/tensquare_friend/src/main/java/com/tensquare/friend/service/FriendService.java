package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交友的业务层
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    @Autowired
    private UserClient userClient;

    /**
     * 添加好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void addFriend(String userid,String friendid){
        //1.使用用户id和好友id查询是否已经添加了过了
        Friend friend = friendDao.findByUseridAndFriendid(userid,friendid);
        if(friend != null){
            throw new IllegalStateException("您已经添加过了，请不要重复添加");
        }
        //2.查询是否已经拉黑
        NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userid,friendid);
        if(friend != null){
            throw new IllegalStateException("您已经把加入了黑名单，不能再添加为好友");
        }
        //3.查询对方是否已经把当前用户拉黑
        noFriend = noFriendDao.findByUseridAndFriendid(friendid,userid);
        if(friend != null){
            throw new IllegalStateException("您已经被加入了黑名单，不能再添加对方为好友");
        }
        //4.添加对方为好友
        friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //5.使用好友id作为用户id，用户id变成好友id查询，看看对方是否已经加当前用户为好友了
        Friend inversefriend = friendDao.findByUseridAndFriendid(friendid,userid);
        if(inversefriend != null){
            //表明对方已经加当前用户为好友了，需要更新islike为1
            friendDao.updateIslike(userid,friendid,"1");
            friendDao.updateIslike(friendid,userid,"1");
        }
        //6.更新当前用户的关注数+1
        userClient.updateFollow(userid,1);
        //7.更新当前好友的粉丝数+1
        userClient.updateFans(friendid,1);
    }

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void deleteFriend(String userid,String friendid){
        //1.删除好友
        friendDao.deleteFriend(userid,friendid);
        //2.看看是不是双向喜欢
        Friend friend = friendDao.findByUseridAndFriendid(friendid,userid);
        if(friend != null){
            //更新对方的islike是0
            friendDao.updateIslike(friendid,userid,"0");
        }
        //3.更新当前用户的关注数-1
        userClient.updateFollow(userid,-1);
        //4.更新当前好友的粉丝数-1
        userClient.updateFans(friendid,-1);
    }

    /**
     * 添加非好友（拉黑）
     * @param userid
     * @param friendid
     */
    public void addNoFriend(String userid,String friendid){
        //1.判断是否已经拉黑过，不让重复拉黑
        NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userid,friendid);
        if(noFriend != null){
            throw new IllegalStateException("请不要重复拉黑");
        }
        //2.判断对方是否已经先拉黑，只要有一方拉黑就不用在添加了
        noFriend = noFriendDao.findByUseridAndFriendid(friendid,userid);
        if(noFriend != null){
            throw new IllegalStateException("对方已经把你先拉黑了");
        }
        //3.判断是否已经加为了好友，如果加为了好友，可以选择不让拉黑，或者是删除好友再拉黑
        Friend friend = friendDao.findByUseridAndFriendid(userid,friendid);
        if(friend != null){
            throw new IllegalStateException("你已经添加了对方为好友，请先删除好友，再选择拉黑");
        }
        friend = friendDao.findByUseridAndFriendid(friendid,userid);
        if(friend != null){
            throw new IllegalStateException("对方已经把你添加为好友了！");
        }
        //4.添加非好友记录
        noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
















}
