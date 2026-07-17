package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.OrderByTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 10/16/2017.
 */
public class ItemInStatusTO extends ResponseData {
    private Integer status_id;
    private Integer offset;
    private Integer count;
    private OrderByTO order;

    public ItemInStatusTO() {
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
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

    public OrderByTO getOrder() {
        return order;
    }

    public void setOrder(OrderByTO order) {
        this.order = order;
    }
}
