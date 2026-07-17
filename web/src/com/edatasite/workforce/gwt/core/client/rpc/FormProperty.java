package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class FormProperty implements IsSerializable {

    private String code;
    private String title;
    private String aliasName;
    private boolean changed;
    private boolean required;
    private String widget;
    private Integer selectedId;
    private String defaultValue;
    private boolean disabled = false;
    private ArrayList<Integer> roleEdit;
    private ArrayList<SelectItem> allRoles;
    private boolean systemRequired;
    private String minChar;
    private Boolean information;
    private String informationText;
    private boolean approvalRelated;
    private String lang;
    private String titleEn;
    private String titleRu;
    private String titleAr;
    private String titleUz;
    private String[] predefinedValues;
    private SelectItem[] systemPredefinedValues;
    private CustomFieldLookUpTypeEnum lookUpType;
    private String query;

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAliasName() {
        return this.aliasName;
    }

    public void setAliasName(final String aliasName) {
        this.aliasName = aliasName;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(final boolean changed) {
        this.changed = changed;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public String getWidget() {
        return this.widget;
    }

    public void setWidget(final String widget) {
        this.widget = widget;
    }

    public Integer getSelectedId() {
        return this.selectedId;
    }

    public void setSelectedId(final Integer selectedId) {
        this.selectedId = selectedId;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    public ArrayList<Integer> getRoleEdit() {
        return this.roleEdit;
    }

    public void setRoleEdit(ArrayList<Integer> roleEdit) {
        this.roleEdit = roleEdit;
    }

    public ArrayList<SelectItem> getAllRoles() {
        return this.allRoles;
    }

    public void setAllRoles(ArrayList<SelectItem> allRoles) {
        this.allRoles = allRoles;
    }

    public boolean isSystemRequired() {
        return systemRequired;
    }

    public void setSystemRequired(boolean systemRequired) {
        this.systemRequired = systemRequired;
    }

    public String getMinChar() {
        return minChar;
    }

    public void setMinChar(String minChar) {
        this.minChar = minChar;
    }

    public boolean isInformation() {
        return information != null ? information : false;
    }

    public void setInformation(boolean information) {
        this.information = information;
    }

    public String getInformationText() {
        return informationText;
    }

    public void setInformationText(String informationText) {
        this.informationText = informationText;
    }

    public boolean isApprovalRelated() {
        return approvalRelated;
    }

    public void setApprovalRelated(boolean approvalRelated) {
        this.approvalRelated = approvalRelated;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(String titleRu) {
        this.titleRu = titleRu;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleUz() {
        return titleUz;
    }

    public void setTitleUz(String titleUz) {
        this.titleUz = titleUz;
    }

    public String[] getPredefinedValues() {
        return predefinedValues;
    }

    public void setPredefinedValues(String[] predefinedValues) {
        this.predefinedValues = predefinedValues;
    }

    public SelectItem[] getSystemPredefinedValues() {
        return systemPredefinedValues;
    }

    public void setSystemPredefinedValues(SelectItem[] systemPredefinedValues) {
        this.systemPredefinedValues = systemPredefinedValues;
    }

    public CustomFieldLookUpTypeEnum getLookUpType() {
        return lookUpType;
    }

    public void setLookUpType(CustomFieldLookUpTypeEnum lookUpType) {
        this.lookUpType = lookUpType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
