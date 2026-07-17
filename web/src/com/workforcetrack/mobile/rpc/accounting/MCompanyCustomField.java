package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 27.09.11
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MCompanyCustomField {

    private Integer objectID;
    private Integer companyID;
    private String entityName;
    private String fieldName;
    private String dataType;
    private String uiType;
    private String[] predefinedValues;
    private boolean showInListing;
    private boolean clickable;
    private boolean showInFilterGrouping;
    private String columnCode;
    private String fieldStringValue;
    private Date fieldDateValue;
    private Integer customFieldSettingID;
    private Integer relationship;

    public MCompanyCustomField() {
    }

    public MCompanyCustomField(CompanyCustomFieldItem item) {

        this.objectID = item.getObjectId();
        this.companyID = item.getCompanyId();
        this.entityName = item.getEntityName();
        this.fieldName = item.getFieldName();
        this.dataType = item.getDataType();
        this.uiType = item.getUiType();
        this.predefinedValues = item.getPredefinedValues();
        this.showInListing = item.isShowInListing();
        this.clickable = item.isClickable();
        this.showInFilterGrouping = item.isShowInFilterGrouping();
        this.columnCode = item.getColumnCode();
        this.fieldStringValue = item.getFieldStringValue();
        this.fieldDateValue = item.getFieldDateNonConvertedValue().getNonConvertedDate();
        this.customFieldSettingID = item.getCustomFieldSettingID();
        this.relationship = item.getRelationship();
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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

    public String[] getPredefinedValues() {
        return predefinedValues;
    }

    public void setPredefinedValues(String[] predefinedValues) {
        this.predefinedValues = predefinedValues;
    }

    public boolean isShowInListing() {
        return showInListing;
    }

    public void setShowInListing(boolean showInListing) {
        this.showInListing = showInListing;
    }

    public boolean isShowInFilterGrouping() {
        return showInFilterGrouping;
    }

    public void setShowInFilterGrouping(boolean showInFilterGrouping) {
        this.showInFilterGrouping = showInFilterGrouping;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
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

    public Integer getCustomFieldSettingID() {
        return customFieldSettingID;
    }

    public void setCustomFieldSettingID(Integer customFieldSettingID) {
        this.customFieldSettingID = customFieldSettingID;
    }

    public Integer getRelationship() {
        return relationship;
    }

    public void setRelationship(Integer relationship) {
        this.relationship = relationship;
    }
}
