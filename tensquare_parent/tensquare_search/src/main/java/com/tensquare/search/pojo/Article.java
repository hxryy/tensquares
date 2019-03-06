package com.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

/**
 * 搜索微服务的文章实体类，它对应的不是关系型数据库的表
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Document(indexName = "tensquare",type = "article")
public class Article implements Serializable {

    @Id
    private String id;//主键

    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_work")
    private String title;
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_work")
    private String content;

    private String state;//状态：有效还是无效

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
