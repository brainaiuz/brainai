package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.OrderByTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Abdurakhmonov Farrukh on 01/29/2018.
 */
public class ContactRequestTO extends ResponseData {
    private Integer offset;
    private Integer count;
    private OrderByTO order;
    private FilteredStatusesRequestTO filter;
    private Integer crmAccountId;

    public ContactRequestTO() {
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

    public FilteredStatusesRequestTO getFilter() {
        return filter;
    }

    public void setFilter(FilteredStatusesRequestTO filter) {
        this.filter = filter;
    }

    public Integer getCrmAccountId() {
        return crmAccountId;
    }

    public void setCrmAccountId(Integer crmAccountId) {
        this.crmAccountId = crmAccountId;
    }
}
