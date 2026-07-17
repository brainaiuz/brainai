package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.FromValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class LeaveRequestStatusTO extends ResponseData {
    private Integer id;
    private String category;
    private String type;
    private FromValueTO data;

    public LeaveRequestStatusTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FromValueTO getData() {
        return data;
    }

    public void setData(FromValueTO data) {
        this.data = data;
    }
}
