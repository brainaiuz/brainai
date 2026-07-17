package com.edatasite.workforce.rest.base.to;

import java.io.Serializable;
import java.util.HashMap;

/**
 * web
 * Created by Sher on 1/30/2015.
 */
public class ParamObject<T> implements Serializable {

    private HashMap<String, Object> params;

    private T object;

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
