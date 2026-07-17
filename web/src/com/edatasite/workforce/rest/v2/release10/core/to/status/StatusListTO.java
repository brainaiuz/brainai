package com.edatasite.workforce.rest.v2.release10.core.to.status;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class StatusListTO<T> extends ResponseData {
    private ArrayList<T> status_list;

    public ArrayList<T> getStatus_list() {
        return status_list;
    }

    public void setStatus_list(ArrayList<T> status_list) {
        this.status_list = status_list;
    }
}

