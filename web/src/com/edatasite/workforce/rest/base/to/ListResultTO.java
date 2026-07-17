package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Umidbek on 17.02.2015.
 */
public class ListResultTO<T> implements IsSerializable {
    Integer totalNumber;
    ArrayList<T> items;

    public ListResultTO() {
    }

    public ListResultTO(Integer totalNumber, ArrayList<T> items) {
        this.totalNumber = totalNumber;
        this.items = items;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }
}
