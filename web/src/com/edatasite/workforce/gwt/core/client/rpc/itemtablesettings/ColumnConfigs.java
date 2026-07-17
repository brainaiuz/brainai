package com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Normurod on 3/13/2017.
 */
public class ColumnConfigs implements IsSerializable {

    private Integer companyCustomFieldID;

    private String code;
    private String title;
    private Integer width;

    private boolean selected;
    private boolean required;
    private boolean disabled;
    private boolean changed;
    private boolean clickable;
    private boolean hasDefault;
    private BigDecimal minValue;

    private String aliasName;
    private String uiType;
    private String dataType;

    private Integer order = 0;

    private ArrayList<Integer> allowedRolesDisabled;
    private ArrayList<Integer> allowedRoles;
    private ArrayList<Integer> allowedRolesView;


    public ColumnConfigs() {

    }

    public ColumnConfigs(String code, String title, boolean required) {
        this.code = code;
        this.title = title;
        this.required = required;
    }

    public ColumnConfigs(String code, String aliasName, String title, boolean required, boolean selected, boolean clickable) {
        this(code, aliasName, title, required);
        this.selected = selected;
        this.clickable = clickable;
    }

    public ColumnConfigs(String code, String aliasName, String title, boolean required) {
        this.code = code;
        this.aliasName = aliasName;
        this.title = title;
        this.required = required;
    }

    public ColumnConfigs(String code, String title, boolean required, Integer width) {
        this(code, title, required);
        this.width = width;
    }

    public Integer getCompanyCustomFieldID() {
        return companyCustomFieldID;
    }

    public void setCompanyCustomFieldID(Integer companyCustomFieldID) {
        this.companyCustomFieldID = companyCustomFieldID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(final boolean changed) {
        this.changed = changed;
    }

    public ArrayList<Integer> getAllowedRolesDisabled() {
        return this.allowedRolesDisabled;
    }

    public void setAllowedRolesDisabled(final ArrayList<Integer> allowedRolesDisabled) {
        this.allowedRolesDisabled = allowedRolesDisabled;
    }

    public ArrayList<Integer> getAllowedRoles() {
        return this.allowedRoles;
    }

    public void setAllowedRoles(final ArrayList<Integer> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    public ArrayList<Integer> getAllowedRolesView() {
        return this.allowedRolesView;
    }

    public void setAllowedRolesView(final ArrayList<Integer> allowedRolesView) {
        this.allowedRolesView = allowedRolesView;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean hasDefault() {
        return hasDefault;
    }

    public void setHasDefault(boolean hasDefault) {
        this.hasDefault = hasDefault;
    }

    public BigDecimal getMinValue() {
        return this.minValue != null ? this.minValue : BigDecimal.ZERO;
    }

    public void setMinValue(final BigDecimal minValue) {
        this.minValue = minValue;
    }
}
