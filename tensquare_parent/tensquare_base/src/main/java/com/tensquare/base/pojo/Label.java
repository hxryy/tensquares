package com.tensquare.base.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 标签的实体类
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Entity
@Table(name="tb_label")
public class Label implements Serializable {

    @Id
    private String id;//主键
    private String labelname;//标签名称
    private String state;//是否可用 1表示可用 0表示不可用
    private String recommend;//是否推荐 1表示推荐 0表示不推荐
    private Integer count;//使用数
    private Integer fans;//关注数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabelname() {
        return labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    @Override
    public String toString() {
        return "Label{" +
                "id='" + id + '\'' +
                ", labelname='" + labelname + '\'' +
                ", state='" + state + '\'' +
                ", recommend='" + recommend + '\'' +
                ", count=" + count +
                ", fans=" + fans +
                '}';
    }
}
