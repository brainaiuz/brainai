package com.edatasite.workforce.rest.v3.release10.crm.dto.lead;

public class LeadConvertTO {
    private Integer id;
    private OpportunityConvertTO opportunity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OpportunityConvertTO getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(OpportunityConvertTO opportunity) {
        this.opportunity = opportunity;
    }
}
