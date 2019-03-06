package com.tensquare.qa.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 问题id和标签id的对应实体类
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Entity
@Table(name="tb_pl")
public class Pl implements Serializable {

    @Id
    private String problemid;
    @Id
    private String labelid;

    public String getProblemid() {
        return problemid;
    }

    public void setProblemid(String problemid) {
        this.problemid = problemid;
    }

    public String getLabelid() {
        return labelid;
    }

    public void setLabelid(String labelid) {
        this.labelid = labelid;
    }

    @Override
    public String toString() {
        return "Pl{" +
                "problemid='" + problemid + '\'' +
                ", labelid='" + labelid + '\'' +
                '}';
    }
}
