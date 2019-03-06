package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签的业务层
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有
     * @return
     */
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    /**
     * 保存
     * @param label
     */
    public void save(Label label){
        //1.给label中的id赋值，使用idworker
        label.setId(String.valueOf(idWorker.nextId()));
        //2.保存
        labelDao.save(label);
    }

    /**
     * 更新
     * @param label
     */
    public void update(Label label){
        labelDao.save(label);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id){
        labelDao.deleteById(id);
    }

    /**
     * 条件查询
     * @param label  封装的条件
     * @return
     */
    public List<Label> findByCondition(Label label){
        //1.定义条件对象
        Specification<Label> spec = this.generateCondition(label);
        //2.调用持久层查询
        return labelDao.findAll(spec);
    }

    /**
     * 分页查询
     * @param label
     * @param page
     * @param size
     * @return
     */
    public Page<Label> findPage(Label label,int page,int size){
        //1.创建条件对象
        Specification<Label> spec = this.generateCondition(label);
        //2.创建分页条件对象
        Pageable pageable = PageRequest.of(page-1,size);
        //3.执行查询并返回
        return labelDao.findAll(spec,pageable);
    }

    /**
     * 生成查询条件
     * @return
     */
    private Specification<Label> generateCondition(Label label){
        return new Specification<Label>() {
            /**
             *
             * @param root  当前要查询的实体类对象的封装，就是Specification定义的泛型对象的封装
             * @param query 查询对象
             * @param cb    查询的条件构建对象
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                ArrayList<Predicate> list = new ArrayList<>();

                //1.判断是否输入了标签名称，输入流就模糊查询：标签名称
                if(!StringUtils.isEmpty(label.getLabelname())){
                    Predicate p1 = cb.like(root.get("labelname"),"%"+label.getLabelname()+"%");
                    list.add(p1);
                }
                //2.判断是否选择状态
                if(!StringUtils.isEmpty(label.getState())){
                    Predicate p2 = cb.equal(root.get("state"),label.getState());
                    list.add(p2);
                }
                //3.判断是否选择推荐
                if(!StringUtils.isEmpty(label.getRecommend())){
                    Predicate p3 = cb.equal(root.get("recommend"),label.getRecommend());
                    list.add(p3);
                }
                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };
    }
}
