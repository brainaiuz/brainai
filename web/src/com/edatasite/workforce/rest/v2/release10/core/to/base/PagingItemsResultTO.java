package com.edatasite.workforce.rest.v2.release10.core.to.base;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 01/17/2018.
 */

public class PagingItemsResultTO<T> extends PagingResultTO {

    private ArrayList<T> items;

    public PagingItemsResultTO() {
    }

    public PagingItemsResultTO(ArrayList<T> items) {
        this.items = items;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }
}

