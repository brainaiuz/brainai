package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: iabdullo
 * Date: 30.09.14 13:58
 */
public class ListingResult<T> implements IsSerializable {
    private T[] list;
    private Integer total = 0;

    public ListingResult() {
    }

    public ListingResult(T[] list) {
        this.list = list;
    }

    public ListingResult(T[] list, Integer total) {
        this.list = list;
        this.total = total;
    }

    public T[] getList() {
        return list;
    }

    public void setList(T[] list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
