package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.profile.ActionTimesTO;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Faxriddin on 8/7/15.
 */
public class HrReminderItem implements IsSerializable {

    private Integer entityType;
    private String fieldValue;
    private Integer emailTemplateId;
    private ArrayList<ActionTimesTO> actionTimes;
    private ArrayList<SelectItem> roleItems;
    private ArrayList<SelectItem> workFlowItems;
    private String fieldCode;
    private Integer onboardingStepId;
    private Integer itemId;

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Integer getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(Integer emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    public ArrayList<ActionTimesTO> getActionTimes() {
        return actionTimes;
    }

    public void setActionTimes(ArrayList<ActionTimesTO> actionTimes) {
        this.actionTimes = actionTimes;
    }

    public ArrayList<SelectItem> getRoleItems() {
        return roleItems;
    }

    public void setRoleItems(ArrayList<SelectItem> roleItems) {
        this.roleItems = roleItems;
    }

    public ArrayList<SelectItem> getWorkFlowItems() {
        return workFlowItems;
    }

    public void setWorkFlowItems(ArrayList<SelectItem> workFlowItems) {
        this.workFlowItems = workFlowItems;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setOnboardingStepId(Integer onboardingStepId) {
        this.onboardingStepId = onboardingStepId;
    }

    public Integer getOnboardingStepId() {
        return onboardingStepId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
