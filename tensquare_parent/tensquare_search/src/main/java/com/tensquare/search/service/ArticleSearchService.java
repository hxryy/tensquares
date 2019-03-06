package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.IdWorker;

/**
 * 文章搜索的业务层
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Service
public class ArticleSearchService {

    @Autowired
    private ArticleSearchDao articleSearchDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存文章到elasticsearch
     * @param article
     */
    public void save(Article article){
        //1.给article的id赋值
        article.setId(String.valueOf(idWorker.nextId()));
        //2.实现保存
        articleSearchDao.save(article);
    }

    /**
     * 查询所有
     * @return
     */
    public Iterable<Article> findAll(){
        return articleSearchDao.findAll();
    }

    /**
     * 根据关键词检索
     * @return
     */
    public Page<Article> findByKeywords(String keywords,int page,int size){
        //1.创建分页对象
        Pageable pageable = PageRequest.of(page-1,size);
        //2.执行查询并返回
        return articleSearchDao.findByTitleLikeOrContentLike(keywords,keywords,pageable);
    }
}
