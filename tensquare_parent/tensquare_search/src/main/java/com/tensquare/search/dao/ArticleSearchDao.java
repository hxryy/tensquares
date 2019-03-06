package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 文章搜索的持久层接口
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String>{

    /**
     * 根据文章的标题或者内容检索
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    Page<Article> findByTitleLikeOrContentLike(String title, String content, Pageable pageable);
}
