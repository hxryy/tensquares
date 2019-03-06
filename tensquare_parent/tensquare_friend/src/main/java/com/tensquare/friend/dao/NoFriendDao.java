package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 非好友的持久层接口
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public interface NoFriendDao extends JpaRepository<NoFriend,String>{

    /**
     * 根据用户id和好友id查询非好友表
     * @param userid
     * @param friendid
     * @return
     */
    NoFriend findByUseridAndFriendid(String userid,String friendid);
}
