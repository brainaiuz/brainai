package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Farrukh Abdurakhmonov on 11/01/2018.
 */
public class ApprovalsListTO<T> extends ResponseData {
    private Integer id;
    private String title;
    private Integer order_id;
    private String request_type;
    private ArrayList<T> requests;

    public ApprovalsListTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public ArrayList<T> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<T> requests) {
        this.requests = requests;
    }
}
