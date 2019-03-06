package com.tensquare.qa.dao;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /**
     * 最新回答的问题列表
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) and p.replytime is not null  order by p.replytime  desc")
    Page<Problem> findNewList(String labelid, Pageable pageable);

    /**
     * 热门回答的问题列表
     * @param labelid
     * @param pageable
     * @return
     */
    @Query(value = "select * from tb_problem where id in(select problemid from tb_pl where labelid = ?1)  and replytime is not null  order by reply desc",nativeQuery = true)
    Page<Problem> findHotList(String labelid,Pageable pageable);

    /**
     * 等待回答的问题列表
     * @param labelid
     * @param pageable
     * @return
     */
    @Query(value = "select * from tb_problem where id in(select problemid from tb_pl where labelid = ?1)  and replytime is null order by createtime desc ",nativeQuery = true)
    Page<Problem> findWaitList(String labelid,Pageable pageable);
}
