package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by Dilsh0d Madrahimov on 3/30/15.
 */
public class CustomFieldTO implements IsSerializable {

    private Integer id;
    private String fieldName;
    private Integer entityId;
    private String entityName;
    private String dataType;
    private String uiType;
    private String fieldStringValue;
    private Date fieldDateValue;
    private String[] predefinedValues;
    private String columnCode;
    private SelectItemTO entityType;


    public CustomFieldTO() {
    }

    public CustomFieldTO(CompanyCustomFieldItem item) {
        this.id = item.getObjectId();
        this.fieldName = item.getFieldName();
        this.entityId = item.getEntityId();
        this.entityName = item.getEntityName();
        this.dataType = item.getDataType();
        this.uiType = item.getUiType();
        this.fieldStringValue = item.getFieldStringValue();
        this.fieldDateValue = item.getFieldDateNonConvertedValue().getNonConvertedDate();
        this.predefinedValues = item.getPredefinedValues();
        this.columnCode = item.getColumnCode();
        if (item.getEntityType() != null) {
            this.entityType = new SelectItemTO(item.getEntityType());
        }
    }

    public CompanyCustomFieldItem convertToCustomField() {
        CompanyCustomFieldItem item = new CompanyCustomFieldItem();
        item.setObjectId(this.id);
        item.setFieldName(this.fieldName);
        item.setEntityId(this.entityId);
        item.setEntityName(this.entityName);
        item.setDataType(this.dataType);
        item.setUiType(this.uiType);
        item.setFieldStringValue(this.fieldStringValue);
        item.setFieldDateNonConvertedValue(new DateNonConvertable(this.fieldDateValue));
        item.setPredefinedValues(this.predefinedValues);
        item.setColumnCode(this.columnCode);
        if (this.entityType != null) {
            SelectItemTO itemTO = this.entityType;
            item.setEntityType(new SelectItem(itemTO.getId(), itemTO.getName()));
        }
        return item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public String getFieldStringValue() {
        return fieldStringValue;
    }

    public void setFieldStringValue(String fieldStringValue) {
        this.fieldStringValue = fieldStringValue;
    }

    public Date getFieldDateValue() {
        return fieldDateValue;
    }

    public void setFieldDateValue(Date fieldDateValue) {
        this.fieldDateValue = fieldDateValue;
    }

    public String[] getPredefinedValues() {
        return predefinedValues;
    }

    public void setPredefinedValues(String[] predefinedValues) {
        this.predefinedValues = predefinedValues;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public SelectItemTO getEntityType() {
        return entityType;
    }

    public void setEntityType(SelectItemTO entityType) {
        this.entityType = entityType;
    }

}
