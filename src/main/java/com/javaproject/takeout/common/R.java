package com.javaproject.takeout.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 *  Common return result
 *  @param <T>
 **/
@Data
public class R<T> {

    private Integer code; //1 success

    private String msg; //error message

    private T data;

    private Map map = new HashMap(); //dynamic data

    // use R.success
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    // use R.error
    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }
    
    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
