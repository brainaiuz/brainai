package com.edatasite.workforce.rest.v2.release10.core.to.base;

import java.util.List;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class ResponseListData<T> extends ResponseData {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public ResponseListData() {

    }

    public ResponseListData(List<T> list) {
        this.list = list;
    }
}

