package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LogHistoryItem implements IsSerializable, Serializable {
    private static final long serialVersionUID = 1L;


    private Integer entityID;
    private String entityType;
    private Integer userID;
    private String userName;
    private Integer creatorID;
    private String creator;
    private String field;
    private String fromStringValue;
    private BigDecimal fromNumberValue;
    private Date fromDateValue;
    private String toStringValue;
    private BigDecimal toNumberValue;
    private Date toDateValue;
    private Date updatedDate;

    public LogHistoryItem() {}

    public Integer getEntityID() {
        return this.entityID;
    }

    public void setEntityID(final Integer entityID) {
        this.entityID = entityID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getUserID() {
        return this.userID;
    }

    public void setUserID(final Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getField() {
        return this.field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public String getFromStringValue() {
        return this.fromStringValue;
    }

    public void setFromStringValue(final String fromStringValue) {
        this.fromStringValue = fromStringValue;
    }

    public BigDecimal getFromNumberValue() {
        return this.fromNumberValue;
    }

    public void setFromNumberValue(final BigDecimal fromNumberValue) {
        this.fromNumberValue = fromNumberValue;
    }

    public Date getFromDateValue() {
        return this.fromDateValue;
    }

    public void setFromDateValue(final Date fromDateValue) {
        this.fromDateValue = fromDateValue;
    }

    public String getToStringValue() {
        return this.toStringValue;
    }

    public void setToStringValue(final String toStringValue) {
        this.toStringValue = toStringValue;
    }

    public BigDecimal getToNumberValue() {
        return this.toNumberValue;
    }

    public void setToNumberValue(final BigDecimal toNumberValue) {
        this.toNumberValue = toNumberValue;
    }

    public Date getToDateValue() {
        return this.toDateValue;
    }

    public void setToDateValue(final Date toDateValue) {
        this.toDateValue = toDateValue;
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(final Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getCreatorID() {
        return this.creatorID;
    }

    public void setCreatorID(final Integer creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(final String creator) {
        this.creator = creator;
    }

    public String getColumnValue(boolean fromValue) {
        if (fromValue) {
            if (getFromDateValue() != null) {
                return DateUtils.format(getFromDateValue());
            } else if (getFromNumberValue() != null) {
                return String.valueOf(getFromNumberValue().intValue());
            } else if (getFromStringValue() != null && !"".equals(getFromStringValue())) {
                return getFromStringValue().replace("|", "");
            }
        } else {
            if (getToDateValue() != null) {
                return DateUtils.format(getToDateValue());
            } else if (getToNumberValue() != null) {
                return String.valueOf(getToNumberValue().intValue());
            } else if (getToStringValue() != null && !"".equals(getToStringValue())) {
                return getToStringValue().replace("|", "");
            }
        }
        return "N/A";
    }
}



