package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Azazello on 10/6/16.
 */
public class WorkflowInvoiceField implements IsSerializable {
    private Integer objectID;
    private String action;
    private String value;
    private String customFieldID;
    private boolean percentage;
    private boolean demandOn;
    private ModelField field;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCustomFieldID() {
        return customFieldID;
    }

    public void setCustomFieldID(String customFieldID) {
        this.customFieldID = customFieldID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public void setPercentage(boolean percentage) {
        this.percentage = percentage;
    }

    public boolean isDemandOn() {
        return demandOn;
    }

    public void setDemandOn(boolean demandOn) {
        this.demandOn = demandOn;
    }

    public ModelField getField() {
        return field;
    }

    public void setField(ModelField field) {
        this.field = field;
    }
}
