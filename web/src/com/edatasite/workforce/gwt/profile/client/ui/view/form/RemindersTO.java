package com.edatasite.workforce.gwt.profile.client.ui.view.form;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: abror
 * Date: 8/3/15 2:44 PM
 */
public class RemindersTO implements IsSerializable {
    private Integer stepId;
    private String stepName;
//    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private SelectItem[] customField;

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

//    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
//        return customFieldItems;
//    }
//
//    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
//        this.customFieldItems = customFieldItems;
//    }

    public SelectItem[] getCustomField() {
        return customField;
    }

    public void setCustomField(SelectItem[] customField) {
        this.customField = customField;
    }
}
