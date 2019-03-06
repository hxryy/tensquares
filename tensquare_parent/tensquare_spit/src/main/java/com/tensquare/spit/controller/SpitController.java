package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 吐槽微服务的控制器
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@RestController
@RequestMapping("/spit")
@CrossOrigin//跨域访问
public class SpitController {

    @Autowired
    private SpitService spitService;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        //1.调用业务层执行操作
        List<Spit> spits = spitService.findAll();
        //2.创建返回值并返回
        return new Result(true, StatusCode.OK,"查询成功",spits);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public Result findById(@PathVariable("id") String id){
        //1.调用业务层执行操作
        Spit spit = spitService.findById(id);
        //2.创建返回值并返回
        return new Result(true, StatusCode.OK,"查询成功",spit);
    }

    /**
     * 保存
     * @param spit
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        //1.调用业务层执行操作
        spitService.save(spit);
        //2.创建返回值并返回
        return new Result(true, StatusCode.OK,"保存成功");
    }


    /**
     * 更新
     * @param spit
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/{id}")
    public Result update(@RequestBody Spit spit,@PathVariable("id") String id){
        //给spit对象的id赋值
        spit.set_id(id);
        //1.调用业务层执行操作
        spitService.update(spit);
        //2.创建返回值并返回
        return new Result(true, StatusCode.OK,"更新成功");
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE,value = "/{id}")
    public Result delete(@PathVariable("id") String id){
        //1.调用业务层执行操作
        spitService.delete(id);
        //2.创建返回值并返回
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /**
     * 根据上级id查询吐槽列表
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/comment/{parentid}/{page}/{size}")
    public Result findByParentid(@PathVariable("parentid") String parentid,@PathVariable("page") int page,@PathVariable("size") int size){
        //1.调用业务层查询
        Page<Spit> spitPage = spitService.findByParentid(parentid,page,size);
        //2.创建自定义的分页对象
        PageResult<Spit> pageResult = new PageResult<>(spitPage.getTotalElements(),spitPage.getContent());
        //3.创建返回值并返回
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 吐槽点赞
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/thumbup/{id}")
    public Result thumbup(@PathVariable("id") String id){
        //判断当前登录的用户是否点过赞了
        String userid = "1001";
        Object value = redisTemplate.opsForValue().get("userid_"+userid+"_spitid_"+id);
        if(value != null){
            throw new IllegalStateException("请不要重复点赞");
        }
        //1.调用业务层点赞
        spitService.updateThumbup(id);
        redisTemplate.opsForValue().set("userid_"+userid+"_spitid_"+id,"thumbup",24, TimeUnit.HOURS);
        //2.创建返回值并返回
        return new Result(true,StatusCode.OK,"点赞成功");
    }
}
