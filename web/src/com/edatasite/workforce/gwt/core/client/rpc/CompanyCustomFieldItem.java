package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Ilxom Lutfullaev
 * Date: Mar 24, 2010
 * Time: 4:06:55 PM
 */

public class CompanyCustomFieldItem implements IsSerializable, Serializable {

    public static final String TEXT = Constants.DATA_TYPE_TEXT;
    public static final String NUMBER = Constants.DATA_TYPE_NUMBER;
    public static final String DATE = Constants.DATA_TYPE_DATE;
    private Integer objectId;
    private Integer entityId;// use as objectId
    private Integer companyId;
    private String entityName;
    private String fieldName;
    private String defaultName;
    private String aliasName;
    private String dataType;
    private Integer columnWidth;
    private String uiType;
    private String[] predefinedValues;
    private SelectItem[] predefinedValuesWithSorting;
    private String query;
    private boolean showInListing;
    private boolean show;
    private boolean deleted;
    private boolean showInFilterGrouping;
    private String columnCode;
    private String fieldStringValue;
    //    private Date fieldDateValue;
    private DateNonConvertable fieldDateNonConvertedValue;
    private Integer customFieldSettingID;
    private Integer relationship;
    private String relationshipName;
    private boolean fieldNameExists = false;
    private boolean aliasNameExists = false;
    private boolean customFieldAdded = false;
    private boolean isFacetable;
    private boolean isRequired;
    private boolean isAddTab;
    private boolean isSeeOwnPermission;
    private boolean isCustomForm;
    private ArrayList<Integer> allowedRoles;
    private ArrayList<SelectItem> roleList;
    private ArrayList<SelectItem> allRoles;
    private ArrayList<Integer> roleEdit;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdatedDate;
    private String section;

    private CustomFieldSettingItem[] listeners;
    private CustomFieldSettingItem[] validations;
    private SelectItem[] queryItems;
    private String localeCode;
    private FileItem[] attachments;
    private FileResource[] itemattachments;
    private Integer fileUploadFieldId;
    private String entityCategoryName;
    private String entityCategoryAlias;
    private boolean clickable;
    private SelectItem entityType;
    private CustomFieldLookUpTypeEnum lookUpTypeEnum;
    private Integer selectedId;
    private String defaultValue;
    private String prefix;
    private String minHeight;
    private boolean disabled;
    private Boolean useInPermission;
    private Integer profielImageId;
    private SelectItem item;
    private ArrayList<SelectItem> selectItems;
    private ArrayList<String> fileUrls;
    private SelectItem referenceItem;
    private boolean systemField;
    private boolean active;
    private Integer scale;
    private Integer relationFieldId;
    private String relationFieldValues;
    private String form;
    private String parentFieldName;
    private CustomFormLocalization localization;
    private String quizFormScoreValues;
    private Double numberMinValue;
    private String userLocale;
    private String minChar;
    private HashMap<String, ArrayList<SelectItem>> relationItemsMap = new HashMap<>();
    private SelectItem customLogicField;
    private String customLogicValue;

    public CompanyCustomFieldItem() {

    }

    public CompanyCustomFieldItem(Integer objectId, String fieldStringValue) {
        this.objectId = objectId;
        this.fieldStringValue = fieldStringValue;
    }

//    public CompanyCustomFieldItem(Integer objectId, Date fieldDateValue) {
//        this.objectId = objectId;
//        this.fieldDateValue = fieldDateValue;
//    }

    public CompanyCustomFieldItem(Integer objectID) {
        this.objectId = objectID;
    }

    public static HashMap<String, CompanyCustomFieldItem> asMap(ArrayList<CompanyCustomFieldItem> customFields) {
        HashMap<String, CompanyCustomFieldItem> result = new HashMap<>();
        if (customFields != null && customFields.size() > 0) {
            for (CompanyCustomFieldItem item : customFields) {
                if (item != null) {
                    result.put(item.getFieldName(), item);
                }
            }
        }
        return result;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
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

    public SelectItem[] getPredefinedValuesWithSorting() {
        return predefinedValuesWithSorting;
    }

    public void setPredefinedValuesWithSorting(SelectItem[] predefinedValuesWithSorting) {
        this.predefinedValuesWithSorting = predefinedValuesWithSorting;
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

    public boolean isFacetable() {
        return isFacetable;
    }

    public void setFacetable(boolean facetable) {
        isFacetable = facetable;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isAddTab() {
        return this.isAddTab;
    }

    public void setAddTab(final boolean addTab) {
        this.isAddTab = addTab;
    }

    public boolean isCustomForm() {
        return this.isCustomForm;
    }

    public void setCustomForm(final boolean customForm) {
        this.isCustomForm = customForm;
    }

    public ArrayList<Integer> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(ArrayList<Integer> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public void setFieldStringValue(String fieldStringValue, boolean... addIfNotNullAndRemoveDuplicates) {
        if (fieldStringValue != null && !"".equals(fieldStringValue)) {
            if (addIfNotNullAndRemoveDuplicates != null && addIfNotNullAndRemoveDuplicates.length > 0 && addIfNotNullAndRemoveDuplicates[0] && this.fieldStringValue != null) {
                if (addIfNotNullAndRemoveDuplicates.length > 1 && addIfNotNullAndRemoveDuplicates[1]) {
                    if (this.fieldStringValue.matches("^(((.)*[,])?)(" + fieldStringValue + ")(([,](.)*)?)$")) {
                        fieldStringValue = "";
                    }
                }
                if (!"".equals(fieldStringValue)) {
                    this.fieldStringValue += ("".equals(this.fieldStringValue) ? "" : "-:-") + fieldStringValue;
                }
            } else {
                this.fieldStringValue = fieldStringValue;
            }
        } else {
            this.fieldStringValue = "";
        }
    }

    public void setFieldStringValue(Double fieldDoubleValue, boolean... withOutDotPart) {
        if (fieldDoubleValue != null) {
            if (withOutDotPart != null && withOutDotPart.length > 0 && withOutDotPart[0] && fieldDoubleValue - fieldDoubleValue.intValue() == 0) {
                this.fieldStringValue = "" + (int) Math.ceil(fieldDoubleValue);
            } else {
                this.fieldStringValue = String.valueOf(fieldDoubleValue.doubleValue());
            }
        } else {
            this.fieldStringValue = null;
        }
    }

    public boolean isFieldNameExists() {
        return fieldNameExists;
    }

    public void setFieldNameExists(boolean fieldNameExists) {
        this.fieldNameExists = fieldNameExists;
    }

    public boolean isAliasNameExists() {
        return aliasNameExists;
    }

    public void setAliasNameExists(boolean aliasNameExists) {
        this.aliasNameExists = aliasNameExists;
    }

    public boolean isCustomFieldAdded() {
        return customFieldAdded;
    }
//
//    public Date getFieldDateValue() {
//        return fieldDateValue;
//    }
//
//    public void setFieldDateValue(Date fieldDateValue) {
//        this.fieldDateValue = fieldDateValue;
//    }

    public void setCustomFieldAdded(boolean customFieldAdded) {
        this.customFieldAdded = customFieldAdded;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public FileResource[] getItemattachments() {
        return itemattachments;
    }

    public void setItemattachments(FileResource[] itemattachments) {
        this.itemattachments = itemattachments;
    }

    public DateNonConvertable getFieldDateNonConvertedValue() {
        return fieldDateNonConvertedValue;
    }

    public void setFieldDateNonConvertedValue(DateNonConvertable fieldDateNonConvertedValue) {
        this.fieldDateNonConvertedValue = fieldDateNonConvertedValue;
    }

    public Integer getCustomFieldSettingID() {
        return customFieldSettingID;
    }

    public void setCustomFieldSettingID(Integer customFieldSettingID) {
        this.customFieldSettingID = customFieldSettingID;
    }

    public CustomFieldSettingItem[] getListeners() {
        return listeners;
    }

    public void setListeners(CustomFieldSettingItem[] listeners) {
        this.listeners = listeners;
    }

    public CustomFieldSettingItem[] getValidations() {
        return validations;
    }

    public void setValidations(CustomFieldSettingItem[] validations) {
        this.validations = validations;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SelectItem[] getQueryItems() {
        return queryItems;
    }

    public void setQueryItems(SelectItem[] queryItems) {
        this.queryItems = queryItems;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public boolean isInPredefinedValues() {
        if (getPredefinedValues() == null) {
            return true;
        } else {
            for (String predefinedValue : getPredefinedValues()) {
                if (getFieldStringValue() != null && getFieldStringValue().equals(predefinedValue)) {
                    return true;
                }
            }
            return false;
        }
    }

    public Integer getRelationship() {
        return relationship;
    }

    public void setRelationship(Integer relationship) {
        this.relationship = relationship;
    }

    public Integer getFileUploadFieldId() {
        return fileUploadFieldId;
    }

    public void setFileUploadFieldId(Integer fileUploadFieldId) {
        this.fileUploadFieldId = fileUploadFieldId;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public SelectItem getItem() {
        return this.item;
    }

    public void setItem(final SelectItem item) {
        this.item = item;
    }

    public ArrayList<SelectItem> getSelectItems() {
        return this.selectItems;
    }

    public void setSelectItems(ArrayList<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public CompanyCustomFieldItem cloneObject() {
        CompanyCustomFieldItem c = new CompanyCustomFieldItem();
        c.setFieldStringValue(this.fieldStringValue);
//        c.setFieldDateValue(this.fieldDateValue);
        c.setFieldDateNonConvertedValue(this.fieldDateNonConvertedValue);
        c.setColumnCode(this.columnCode);
        c.setCompanyId(this.companyId);
        c.setCustomFieldSettingID(this.customFieldSettingID);
        c.setDataType(this.dataType);
        c.setEntityName(this.entityName);
        c.setFieldName(this.fieldName);
        c.setAliasName(this.aliasName);
        c.setPredefinedValues(this.predefinedValues);
        c.setPredefinedValuesWithSorting(this.predefinedValuesWithSorting);
        c.setUiType(this.uiType);
        c.setShowInFilterGrouping(this.showInFilterGrouping);
        c.setShowInListing(this.showInListing);
        c.setRelationship(this.relationship);
        c.setCreatedBy(this.createdBy);
        c.setCreationDate(this.creationDate);
        c.setLastUpdatedBy(this.lastUpdatedBy);
        c.setLastUpdatedDate(this.lastUpdatedDate);
        c.setAttachments(this.attachments);
        c.setFileUploadFieldId(this.fileUploadFieldId);
        c.setEntityCategoryName(this.entityCategoryName);
        c.setLookUpTypeEnum(this.lookUpTypeEnum);
        c.setDisabled(this.disabled);
        c.setProfielImageId(this.profielImageId);
        c.setReferenceItem(this.getReferenceItem());
        c.setScale(this.getScale());
        return c;
    }

    public String getEntityCategoryName() {
        return entityCategoryName;
    }

    public void setEntityCategoryName(String entityCategoryName) {
        this.entityCategoryName = entityCategoryName;
    }

    public String getEntityCategoryAlias() {
        return entityCategoryAlias;
    }

    public void setEntityCategoryAlias(String entityCategoryAlias) {
        this.entityCategoryAlias = entityCategoryAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyCustomFieldItem)) return false;

        CompanyCustomFieldItem that = (CompanyCustomFieldItem) o;

        return aliasName.equals(that.aliasName) && dataType.equals(that.dataType) && uiType.equals(that.uiType);
    }

    @Override
    public int hashCode() {
        int result = aliasName.hashCode();
        result = 31 * result + dataType.hashCode();
        result = 31 * result + uiType.hashCode();
        return result;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public SelectItem getEntityType() {
        return entityType;
    }

    public void setEntityType(SelectItem entityType) {
        this.entityType = entityType;
    }

    public CustomFieldLookUpTypeEnum getLookUpTypeEnum() {
        return lookUpTypeEnum;
    }

    public void setLookUpTypeEnum(CustomFieldLookUpTypeEnum lookUpTypeEnum) {
        this.lookUpTypeEnum = lookUpTypeEnum;
    }

    public Integer getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Integer selectedId) {
        this.selectedId = selectedId;
    }

    public ArrayList<SelectItem> getRoleList() {
        return roleList;
    }

    public void setRoleList(ArrayList<SelectItem> roleList) {
        this.roleList = roleList;
    }

    public String getDefaultValue() {
        if (defaultValue == null) {
            defaultValue = "";
        }
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getProfielImageId() {
        return profielImageId;
    }

    public void setProfielImageId(Integer profielImageId) {
        this.profielImageId = profielImageId;
    }

    public ArrayList<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(ArrayList<String> fileUrls) {
        this.fileUrls = fileUrls;
    }

    public SelectItem getReferenceItem() {
        return referenceItem;
    }

    public void setReferenceItem(SelectItem referenceItem) {
        this.referenceItem = referenceItem;
    }

    public boolean isSystemField() {
        return systemField;
    }

    public void setSystemField(boolean systemField) {
        this.systemField = systemField;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public ArrayList<SelectItem> getAllRoles() {
        return this.allRoles;
    }

    public void setAllRoles(final ArrayList<SelectItem> allRoles) {
        this.allRoles = allRoles;
    }

    public ArrayList<Integer> getRoleEdit() {
        return this.roleEdit;
    }

    public void setRoleEdit(final ArrayList<Integer> roleEdit) {
        this.roleEdit = roleEdit;
    }

    public String getMinHeight() {
        return this.minHeight;
    }

    public void setMinHeight(final String minHeight) {
        this.minHeight = minHeight;
    }

    public boolean isSeeOwnPermission() {
        return isSeeOwnPermission;
    }

    public void setSeeOwnPermission(boolean seeOwnPermission) {
        isSeeOwnPermission = seeOwnPermission;
    }

    public Integer getRelationFieldId() {
        return relationFieldId;
    }

    public void setRelationFieldId(Integer relationFieldId) {
        this.relationFieldId = relationFieldId;
    }

    public String getRelationFieldValues() {
        return relationFieldValues;
    }

    public void setRelationFieldValues(String relationFieldValues) {
        this.relationFieldValues = relationFieldValues;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getParentFieldName() {
        return parentFieldName;
    }

    public void setParentFieldName(String parentFieldName) {
        this.parentFieldName = parentFieldName;
    }

    public CustomFormLocalization getLocalization() {
        return localization;
    }

    public void setLocalization(CustomFormLocalization localization) {
        this.localization = localization;
    }

    public ArrayList<SelectItem> getChildItemsFromParent(String parentValue) {
//        ArrayList<SelectItem> values = new ArrayList<>();
//        String[] relationFieldsSplit = getRelationFieldValues() != null ? getRelationFieldValues().split(";") : new String[]{};
//        if (relationFieldsSplit.length > 0) {
//            for (int i = 0; i < relationFieldsSplit.length; i++) {
//                String[] str = relationFieldsSplit[i].split("=");
//                if (str.length > 1 && parentValue.equals(str[1])) {
//                    values.add(new SelectItem(i, str[0]));
//                }
//            }
//        }
        return relationItemsMap.get(parentValue);
    }

    public HashMap<String, ArrayList<SelectItem>> getRelationItemsMap() {
        return relationItemsMap;
    }

    public void setRelationItemsMap(HashMap<String, ArrayList<SelectItem>> relationItemsMap) {
        this.relationItemsMap = relationItemsMap;
    }

    public String getQuizFormScoreValues() {
        return quizFormScoreValues;
    }

    public void setQuizFormScoreValues(String quizFormScoreValues) {
        this.quizFormScoreValues = quizFormScoreValues;
    }

    public Double getNumberMinValue() {
        return numberMinValue;
    }

    public void setNumberMinValue(Double numberMinValue) {
        this.numberMinValue = numberMinValue;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(String userLocale) {
        this.userLocale = userLocale;
    }

    public String getMinChar() {
        return minChar;
    }

    public void setMinChar(String minChar) {
        this.minChar = minChar;
    }

    public Boolean isUseInPermission() {
        return useInPermission;
    }

    public void setUseInPermission(Boolean useInPermission) {
        this.useInPermission = useInPermission;
    }

    public void setFieldStringValue(String fieldStringValue) {
        this.fieldStringValue = fieldStringValue;
    }

    public Boolean getUseInPermission() {
        return useInPermission;
    }

    public SelectItem getCustomLogicField() {
        return customLogicField;
    }

    public void setCustomLogicField(SelectItem customLogicField) {
        this.customLogicField = customLogicField;
    }

    public String getCustomLogicValue() {
        return customLogicValue;
    }

    public void setCustomLogicValue(String customLogicValue) {
        this.customLogicValue = customLogicValue;
    }
}
