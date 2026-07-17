package com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class BenefitRequestTO extends ResponseData {
    private Integer id;
    private String title;
    private String approver;
    private String requester;
    private String description;
    private Object status;
    private String employeeImgURL;
    private SelectItem quantity;

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

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getEmployeeImgURL() {
        return employeeImgURL;
    }

    public void setEmployeeImgURL(String employeeImgURL) {
        this.employeeImgURL = employeeImgURL;
    }

    public SelectItem getQuantity() {
        return quantity;
    }

    public void setQuantity(SelectItem quantity) {
        this.quantity = quantity;
    }
}
