package com.lion.canvas.path.common;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2019/3/7
 */
@Data
public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int SUCCESS = 0;

    public static final int FAIL = 1;

    public static final int NO_PERMISSION = 2;

    private String msg = "success";

    private int code = SUCCESS;

    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL ;
    }

    public static ResultBean success(){
        return new ResultBean();
    }

    public static <T> ResultBean<T> success(T data){
        return new ResultBean(data);
    }
    public static ResultBean success(List<Map<String, Object>> data, boolean mapLowerKey){
        if (mapLowerKey){
            List<Map<String, Object>> res = new ArrayList<>();
            data.stream().forEach(map -> res.add(PageResultBean.transformUpperCase((Map<String, Object>)map)));
            return new ResultBean(res);
        }else{
            return new ResultBean(data);
        }
    }
    public static ResultBean error(Throwable e){
        return new ResultBean(e);
    }
}
