package com.edatasite.workforce.gwt.core.server.db.impl;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 06.10.11
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
//Bu listni ozini va totalCounti ni olib beradi.Hamma Listing uchun
public class ListingObjectItem<T> implements IsSerializable {

    private List<T> items;
    private Integer totalCount = 0;

    public ListingObjectItem() {

    }

    public ListingObjectItem(List<T> items, Integer totalCount) {
        this.items = items;
        this.totalCount = totalCount != null ? totalCount.intValue() : 0;
    }

    public ListingObjectItem(List<T> items, Long totalCount) {
        this.items = items;
        this.totalCount = totalCount != null ? totalCount.intValue() : 0;
    }



    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount != null ? totalCount.intValue() : 0;
    }
}
