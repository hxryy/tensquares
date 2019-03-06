package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 更新用户粉丝数
     * @param userid
     * @param fans
     */
    @Modifying
    @Query("update User  set fanscount = fanscount + ?2 where id = ?1 ")
    void updateFans(String userid,int fans);

    /**
     * 更新用户关注数
     * @param userid
     * @param follow
     */
    @Modifying
    @Query("update User set followcount = followcount + ?2 where id = ?1 ")
    void updateFollow(String userid,int follow);
}
