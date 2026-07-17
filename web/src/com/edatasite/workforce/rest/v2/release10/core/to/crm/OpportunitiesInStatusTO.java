package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class OpportunitiesInStatusTO extends ResponseData {
    private Integer status_id;
    private Integer total_count;
    private Integer offset;
    private Integer count;
    private Integer left;
    private ArrayList<OpportunitiesListResultTO> list;

    public OpportunitiesInStatusTO() {
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public ArrayList<OpportunitiesListResultTO> getList() {
        return list;
    }

    public void setList(ArrayList<OpportunitiesListResultTO> list) {
        this.list = list;
    }
}
