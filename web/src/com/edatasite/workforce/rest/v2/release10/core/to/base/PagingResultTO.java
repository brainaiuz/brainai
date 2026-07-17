package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 01/17/2018.
 */
public class PagingResultTO extends ResponseData {
    private Integer total_count;
    private Integer left;
    private Integer count;
    private Integer offset;

    public PagingResultTO() {
    }

    public Integer getTotal_count() {
        if(total_count==null) {
            return  0;
        }
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
