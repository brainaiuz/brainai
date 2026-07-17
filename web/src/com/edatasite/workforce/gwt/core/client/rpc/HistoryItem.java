package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Hurshid on 8/16/2017.
 */
public class HistoryItem implements IsSerializable, Key {
    public static final String ENTITY_ID = "ENTITY_ID";
    public static final String ENTITY_CODE = "ENTITY_CODE";
    public static final String ENTITY_NAME = "ENTITY_NAME";
    public static final String ENTITY_TYPE = "ENTITY_TYPE";
    public static final String FIELD_ID = "PERMISSION_NAME";
    public static final String FROM = "FROM";
    public static final String TO = "TO";
    public static final String MODIFIED_BY = "MODIFIED_BY";
    public static final String MODIFIED_DATE = "MODIFIED_DATE";
    private Integer objectID;
    private String entityType;
    private Integer entityID;
    private String entityName;
    private Integer userID;
    private String employeeCode;
    private String userName;
    private String field;
    private String fromStringValue;
    private BigDecimal fromNumberValue;
    private Date fromDateValue;
    private String toStringValue;
    private BigDecimal toNumberValue;
    private Date toDateValue;
    private Date updatedDate;
    private boolean isContact;
    private String fromReference;
    private String toReference;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFromStringValue() {
        return fromStringValue;
    }

    public void setFromStringValue(String fromStringValue) {
        this.fromStringValue = fromStringValue;
    }

    public BigDecimal getFromNumberValue() {
        return fromNumberValue;
    }

    public void setFromNumberValue(BigDecimal fromNumberValue) {
        this.fromNumberValue = fromNumberValue;
    }

    public Date getFromDateValue() {
        return fromDateValue;
    }

    public void setFromDateValue(Date fromDateValue) {
        this.fromDateValue = fromDateValue;
    }

    public String getToStringValue() {
        return toStringValue;
    }

    public void setToStringValue(String toStringValue) {
        this.toStringValue = toStringValue;
    }

    public BigDecimal getToNumberValue() {
        return toNumberValue;
    }

    public void setToNumberValue(BigDecimal toNumberValue) {
        this.toNumberValue = toNumberValue;
    }

    public Date getToDateValue() {
        return toDateValue;
    }

    public void setToDateValue(Date toDateValue) {
        this.toDateValue = toDateValue;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isContact() {
        return isContact;
    }

    public void setIsContact(boolean isContact) {
        this.isContact = isContact;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFromReference() {
        return fromReference;
    }

    public void setFromReference(String fromReference) {
        this.fromReference = fromReference;
    }

    public String getToReference() {
        return toReference;
    }

    public void setToReference(String toReference) {
        this.toReference = toReference;
    }

    @Override
    public String getKey() {
        return "" + getObjectID();
    }

    public String getColumnValue(boolean fromValue) {
        if (fromValue) {
            if (getFromDateValue() != null) {
                return DateUtils.format(getFromDateValue());
            } else if (getFromNumberValue() != null) {
                return String.valueOf(getFromNumberValue().intValue());
            } else if (getFromStringValue() != null && !"".equals(getFromStringValue())) {
                return getFromStringValue().replace("|", "");
            } else if (getFromReference() != null && !"".equals(getFromReference())) {
                return getFromReference().replace("|", "");
            }
        } else {
            if (getToDateValue() != null) {
                return DateUtils.format(getToDateValue());
            } else if (getToNumberValue() != null) {
                return String.valueOf(getToNumberValue().intValue());
            } else if (getToStringValue() != null && !"".equals(getToStringValue())) {
                return getToStringValue().replace("|", "");
            } else if (getToReference() != null && !"".equals(getToReference())) {
                return getToReference().replace("|", "");
            }
        }
        return "N/A";
    }
}
