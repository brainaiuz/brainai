package com.edatasite.workforce.rest.v2.release10.core.to.status;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class AllStatusListTO<T> extends ResponseData {
    private ArrayList<T> list;

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }
}

