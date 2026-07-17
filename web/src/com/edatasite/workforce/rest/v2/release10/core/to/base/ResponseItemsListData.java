package com.edatasite.workforce.rest.v2.release10.core.to.base;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class ResponseItemsListData<T> extends ResponseData {
    private ArrayList<T> items;

    public ResponseItemsListData() {
    }

    public ResponseItemsListData(ArrayList<T> items) {
        this.items = items;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }
}
