package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 带有分页的结果集信息，
 * 该类中的数据是和前端协商好的
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public class PageResult<T> implements Serializable {

    private Long total;//总记录条数
    private List<T> rows;//带有分页的结果集

    public PageResult(){

    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
