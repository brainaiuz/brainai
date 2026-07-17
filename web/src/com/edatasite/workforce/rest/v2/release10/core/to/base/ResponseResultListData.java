package com.edatasite.workforce.rest.v2.release10.core.to.base;

import java.util.List;

/**
 * Created by Anvar Akramov on 10/27/2017.
 */
public class ResponseResultListData<T> extends ResponseData {

    private List<T> list;
    private Integer total;

    public ResponseResultListData() {
    }

    public ResponseResultListData(List<T> list, Integer total) {
        this.list = list;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}

