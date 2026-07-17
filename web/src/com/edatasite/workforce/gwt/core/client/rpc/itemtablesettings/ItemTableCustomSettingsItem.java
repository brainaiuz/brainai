package com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created By : Dilsh0d Madrahimov on 10/8/2019 2:10 PM
 */
public class ItemTableCustomSettingsItem implements IsSerializable {

    private Integer itemTableSettingsID;
    private Integer customFieldSettingsID;
    private String fieldName;
    private String aliasName;
    private String uiType;
    private Integer width;
    private String dataType;
    private String code;

    private boolean selected;
    private boolean required;

    public Integer getItemTableSettingsID() {
        return itemTableSettingsID;
    }

    public void setItemTableSettingsID(Integer itemTableSettingsID) {
        this.itemTableSettingsID = itemTableSettingsID;
    }

    public Integer getCustomFieldSettingsID() {
        return customFieldSettingsID;
    }

    public void setCustomFieldSettingsID(Integer customFieldSettingsID) {
        this.customFieldSettingsID = customFieldSettingsID;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
