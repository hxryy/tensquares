package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 文章搜索的控制器
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    /**
     * 保存操作
     * @param article
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article){
        //1.调用业务层执行操作
        articleSearchService.save(article);
        //2.创建返回值并返回
        return new Result(true, StatusCode.OK,"保存成功");
    }

    /**
     * 查询所有
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        //1.调用业务层执行操作
        Iterable<Article> iterable = articleSearchService.findAll();
        //2.创建返回值并返回
        return new Result(true,StatusCode.OK,"查询成功",iterable);
    }

    /**
     * 根据关键词查询
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/search/{keywords}/{page}/{size}")
    public Result findByKeywords(@PathVariable("keywords") String keywords,@PathVariable("page") int page,@PathVariable("size") int size){
        //1.调用业务层查询
        Page<Article> articlePage = articleSearchService.findByKeywords(keywords,page,size);
        //2.创建自定义分页对象
        PageResult<Article> pageResult = new PageResult<>(articlePage.getTotalElements(),articlePage.getContent());
        //3.创建返回值结果并返回
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }
}
