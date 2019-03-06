package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签的控制器
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@RestController
@RequestMapping("/label")
@CrossOrigin//支持跨域访问
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 查询所有
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
//        int i=1/0;
        //1.调用业务层操作
        List<Label> labels = labelService.findAll();
        //2.创建返回值对象并返回
        return  new Result(true, StatusCode.OK,"执行成功",labels);
    }


    /**
     * 查询一个
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public Result findById(@PathVariable("id") String id){
//        System.out.println("No2.根据id查询的标签");
        //1.调用业务层操作
        Label label = labelService.findById(id);
        //2.创建返回值对象并返回
        return  new Result(true, StatusCode.OK,"执行成功",label);
    }


    /**
     * 保存
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        //1.调用业务层操作
        labelService.save(label);
        //2.创建返回值对象并返回
        return  new Result(true, StatusCode.OK,"执行成功");
    }


    /**
     * 更新
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/{id}")
    public Result update(@RequestBody Label label,@PathVariable("id") String id){
        //给label中的id赋值
        label.setId(id);
        //1.调用业务层操作
        labelService.update(label);
        //2.创建返回值对象并返回
        return  new Result(true, StatusCode.OK,"执行成功");
    }


    /**
     * 删除
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE,value = "/{id}")
    public Result delete(@PathVariable("id") String id){
        //1.调用业务层操作
        labelService.delete(id);
        //2.创建返回值对象并返回
        return  new Result(true, StatusCode.OK,"执行成功");
    }

    /**
     * 条件查询
     * @param label
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/search")
    public Result findSearch(@RequestBody Label label){
        //1.调用业务层查询
        List<Label> labels = labelService.findByCondition(label);
        //2.创建返回值并返回
        return  new Result(true, StatusCode.OK,"执行成功",labels);
    }

    /**
     * 分页查询
     * @param label
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/search/{page}/{size}")
    public Result findPage(@RequestBody Label label,@PathVariable("page") int page,@PathVariable("size") int size){
        //1.调用业务层查询
        Page<Label> labelPage = labelService.findPage(label,page,size);
        //2.创建我们自定义的分页结果集对象
        PageResult<Label> pageResult = new PageResult<>(labelPage.getTotalElements(),labelPage.getContent());
        //2.创建返回值并返回
        return  new Result(true, StatusCode.OK,"执行成功",pageResult);
    }
}
