package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽的持久层接口
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public interface SpitDao extends MongoRepository<Spit,String> {

    /**
     * 根据上级id查询吐槽列表
     * @param parentid
     * @param pageable
     * @return
     */
    Page<Spit> findByParentid(String parentid, Pageable pageable);
}
