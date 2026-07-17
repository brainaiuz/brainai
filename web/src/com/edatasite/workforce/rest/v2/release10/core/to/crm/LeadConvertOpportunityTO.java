package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;

/**
 * Created by Dilshod Madrahimov on 09/03/2018.
 */
public class LeadConvertOpportunityTO extends ResponseData {
    private Integer assigned_id;
    private String name;
    private BigDecimal amount;
    private Integer status_id;
    private Boolean copy_details;
    private Boolean add_to_account;

    public LeadConvertOpportunityTO() {
    }

    public Integer getAssigned_id() {
        return assigned_id;
    }

    public void setAssigned_id(Integer assigned_id) {
        this.assigned_id = assigned_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Boolean getCopy_details() {
        return copy_details;
    }

    public void setCopy_details(Boolean copy_details) {
        this.copy_details = copy_details;
    }

    public Boolean getAdd_to_account() {
        return add_to_account;
    }

    public void setAdd_to_account(Boolean add_to_account) {
        this.add_to_account = add_to_account;
    }
}
