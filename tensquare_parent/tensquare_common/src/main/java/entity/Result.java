package entity;

import java.io.Serializable;

/**
 * 公共结果返回的对象
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public class Result implements Serializable {

    private boolean flag;//是否成功
    private Integer code;//执行结果状态码
    private String message;//执行结果信息
    private Object data;//响应数据  查询方法才有，而增删改方法没有

    public Result(){

    }

    /**
     * 针对增删改方法
     * @param flag
     * @param code
     * @param message
     */
    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    /**
     * 针对查询方法
     * @param flag
     * @param code
     * @param message
     * @param data
     */
    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "flag=" + flag +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
