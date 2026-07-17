package com.edatasite.workforce.gwt.core.client.rpc.listingpanel;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 06.04.2010
 * Time: 15:00:17
 */
public class ListResult<T> implements IsSerializable {
    private ArrayList<T> list;
    private Integer qTime;
    private Integer total;
    //bu value faqat obshiy bulgan narsalar uchun(masalan : statuslar, typelar, yana allambalolar, har biri uchun emas bittaga yoziladida qolganlari uchun kerak emas...)
    private T defaultOne;
    private T objectData;

    public ListResult() {

    }

    public ListResult(ArrayList<T> list, Integer total) {
        this(list, total, null);
    }

    public ListResult(ArrayList<T> list, Integer total, T objectData) {
        this.list = list;
        if (this.getDefaultOne() == null && this.list != null && this.list.size() > 0) {
            setDefaultOne(this.list.get(0));
        }
        this.total = total;
        this.objectData = objectData;
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }

    public Integer getQueryTime() {
        return qTime;
    }

    public ListResult<T> setQueryTime(Integer time) {
        this.qTime = time;
        return this;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getDefaultOne() {
        return defaultOne;
    }

    public void setDefaultOne(T defaultOne) {
        this.defaultOne = defaultOne;
    }

    public T getObjectData() {
        return objectData;
    }

    public void setObjectData(T objectData) {
        this.objectData = objectData;
    }
}