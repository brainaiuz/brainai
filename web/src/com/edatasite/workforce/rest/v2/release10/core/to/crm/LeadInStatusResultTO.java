package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;

/**
 * Created by Dilsh0d on 10/16/2017.
 */
public class LeadInStatusResultTO extends PagingListResultTO {
    private Integer status_id;

    public LeadInStatusResultTO() {
    }


    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }
}
