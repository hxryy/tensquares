package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * 吐槽的业务层
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    /**
     *  查询所有
     * @return
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /**
     *  根据id查询一个
     * @param id
     * @return
     */
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    /**
     * 保存，此处只是一个基本业务，后面完善
     * @param spit
     */
    public void save(Spit spit){
        //1.给吐槽的主键赋值
        spit.set_id(String.valueOf(idWorker.nextId()));
        //给其他字段提供初始化的值
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        //2.保存
        spitDao.save(spit);
        //3.判断当前吐槽是否有parentid，因为如果有了parentid就表示这条吐槽是一条评论
        if(!StringUtils.isEmpty(spit.getParentid())){
            //当他是真的某条吐槽的评论时，需要对parentid所指向的吐槽回复数+1
            //创建查询对象
            Query query = new Query();
            //设置查询条件
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            //创建更新对象
            Update update = new Update();
            //设置更新内容
            update.inc("comment",1);
            //更新操作
            mongoTemplate.updateFirst(query,update,"spit");
        }
    }

    /**
     * 更新
     * @param spit
     */
    public void update(Spit spit){
        spitDao.save(spit);
    }

    /**
     * 根据id删除
     * @param id
     */
    public void delete(String id){
        spitDao.deleteById(id);
    }

    /**
     * 根据上级id查询吐槽列表 (查询某条吐槽的回复)
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentid,int page,int size){
        //1.创建分页对象
        Pageable pageable = PageRequest.of(page-1,size);
        //2.查询并返回
        return spitDao.findByParentid(parentid,pageable);
    }

    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 吐槽点赞
     * @param id
     */
    public void updateThumbup(String id){
        //1.创建查询对象
        Query query = new Query();
        //2.设置查询的条件
        query.addCriteria(Criteria.where("_id").is(id));//where _id = ?
        //3.创建更新对象
        Update update = new Update();
        //4.设置更新哪个字段和什么内容
        update.inc("thumbup",1);//set thumbup = thumbup+1
        //5.使用MongoTemplate实现更新
        mongoTemplate.updateFirst(query,update,"spit");
    }






}
