package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by shohruh on 25-Mar-17.
 */
public class WorkflowActionItem implements IsSerializable {
    private Integer objectId;

    private Integer fieldId;
    private String fieldIdString;
    private Integer mappedId;
    private Integer defaultId;
    private String defaultText;
    private BigDecimal defaultNumeric;
    private Date defaultDate;

    public WorkflowActionItem() {
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldIdString() {
        return fieldIdString;
    }

    public void setFieldIdString(String fieldIdString) {
        this.fieldIdString = fieldIdString;
    }

    public Integer getMappedId() {
        return mappedId;
    }

    public void setMappedId(Integer mappedId) {
        this.mappedId = mappedId;
    }

    public Integer getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(Integer defaultId) {
        this.defaultId = defaultId;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public BigDecimal getDefaultNumeric() {
        return defaultNumeric;
    }

    public void setDefaultNumeric(BigDecimal defaultNumeric) {
        this.defaultNumeric = defaultNumeric;
    }

    public Date getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
    }
}
