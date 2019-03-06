package com.tensquare.friend.pojo;

import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 交友的实体类
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Entity
@Table(name = "tb_friend")
@IdClass(Friend.class)//表达式当前类是一个id类
public class Friend  implements Serializable{

    @Id
    private String userid;//用户Id
    @Id
    private String friendid;//好友的用户id

    private String islike;//是否为互相关注（喜欢）  0：单向 1：双向

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

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "userid='" + userid + '\'' +
                ", friendid='" + friendid + '\'' +
                ", islike='" + islike + '\'' +
                '}';
    }
}
