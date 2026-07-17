package com.edatasite.workforce.rest.v3.release10.crm.dto.contact;

import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityTO;

import java.util.ArrayList;
import java.util.List;

public class OpportunityByStageTO {
    private Integer stageId;
    private String stageTitle;
    private String probility;
    private Double totalAmount;
    private Integer totalCount;
    private boolean requiredComment = false;
    private List<OpportunityTO> opportunity = new ArrayList<>();

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public String getStageTitle() {
        return stageTitle;
    }

    public void setStageTitle(String stageTitle) {
        this.stageTitle = stageTitle;
    }

    public List<OpportunityTO> getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(List<OpportunityTO> opportunity) {
        this.opportunity = opportunity;
    }

    public String getProbility() {
        return probility;
    }

    public void setProbility(String probility) {
        this.probility = probility;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isRequiredComment() {
        return requiredComment;
    }

    public void setRequiredComment(boolean requiredComment) {
        this.requiredComment = requiredComment;
    }
}
