package com.tensquare.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 非好友的实体类
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Entity
@Table(name = "tb_nofriend")
@IdClass(NoFriend.class)
public class NoFriend implements Serializable {

    @Id
    private String userid;

    @Id
    private String friendid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }
}
