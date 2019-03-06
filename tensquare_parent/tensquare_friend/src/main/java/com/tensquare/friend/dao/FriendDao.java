package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 好友的持久层接口
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public interface FriendDao extends JpaRepository<Friend,String>{

    /**
     * 根据用户id和好友id查询交友表
     * @param userid       用户id
     * @param friendid     好友id
     * @return
     */
    Friend findByUseridAndFriendid(String userid,String friendid);

    /**
     * 更新islike
     * @param userid
     * @param friendid
     * @param islike
     */
    @Modifying
    @Query("update Friend  set islike = ?3 where userid = ?1 and friendid = ?2 ")
    void updateIslike(String userid,String friendid,String islike);

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Modifying
    @Query("delete from Friend where userid = ?1 and friendid = ?2 ")
    void deleteFriend(String userid,String friendid);
}
