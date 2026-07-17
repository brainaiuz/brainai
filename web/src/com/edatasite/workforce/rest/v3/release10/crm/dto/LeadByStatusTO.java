package com.edatasite.workforce.rest.v3.release10.crm.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadTO;

import java.util.ArrayList;
import java.util.List;

public class LeadByStatusTO {
    private Integer statusId;
    private String statusTitle;
    private List<LeadTO> leads = new ArrayList<>();

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

    public List<LeadTO> getLeads() {
        return leads;
    }

    public void setLeads(List<LeadTO> leads) {
        this.leads = leads;
    }
}
