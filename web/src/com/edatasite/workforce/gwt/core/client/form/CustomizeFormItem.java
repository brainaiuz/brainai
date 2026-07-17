package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_ITEM_TABLE;

/**
 * Created by Hurshid on 2/15/2018.
 */
public class CustomizeFormItem implements IsSerializable {

    private Integer id;
    private String section;
    private String name;
    private String label;
    private boolean active;
    private Integer forder;
    private ColumnType columnType;
    private String noLabelFor = "";
    private boolean systemMandatory;
    private boolean custom;
    private boolean customField;
    private String uiType;
    private String dataType;
    private boolean deleted;
    private String entityName;
    private String defaultValue;
    private String formID;
    private boolean isCustomForm;
    private boolean customizableTable;
    private int x;
    private int y;
    private int width;
    private int height;
    private FormProperty formProperty;
    private Boolean isQuizForm;
    private boolean saveVisible;

    public CustomizeFormItem() {
    }

    public CustomizeFormItem(boolean active, String entityName) {
        this.active = active;
        this.entityName = entityName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getForder() {
        return forder;
    }

    public void setForder(Integer forder) {
        this.forder = forder;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public String getNoLabelFor() {
        if (noLabelFor == null) {
            setNoLabelFor("");
        }
        return noLabelFor;
    }

    public void setNoLabelFor(String noLabelFor) {
        this.noLabelFor = noLabelFor;
    }

    public boolean isLabelLess(String formType) {
        return formType != null && getNoLabelFor().toLowerCase().contains(formType.toLowerCase());
    }

    public boolean isSystemMandatory() {
        return systemMandatory;
    }

    public void setSystemMandatory(boolean systemMandatory) {
        this.systemMandatory = systemMandatory;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public boolean isCustomField() {
        return customField;
    }

    public void setCustomField(boolean customField) {
        this.customField = customField;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isCustomForm() {
        return isCustomForm;
    }

    public void setCustomForm(boolean customForm) {
        isCustomForm = customForm;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isItemTable() {
        return getUiType() != null && getUiType().equals(UI_TYPE_ITEM_TABLE) && getName().startsWith(Constants.ITEM_TABLE);
    }

    public String getFormID() {
        return this.formID;
    }

    public void setFormID(final String formID) {
        this.formID = formID;
    }

    public FormProperty getFormProperty() {
        return this.formProperty;
    }

    public void setFormProperty(final FormProperty formProperty) {
        this.formProperty = formProperty;
    }

    public boolean isCustomizableTable() {
        return customizableTable;
    }

    public void setCustomizableTable(boolean customizableTable) {
        this.customizableTable = customizableTable;
    }

    public Boolean getQuizForm() {
        return isQuizForm;
    }

    public void setQuizForm(Boolean quizForm) {
        isQuizForm = quizForm;
    }

    public boolean isSaveVisible() {
        return saveVisible;
    }

    public void setSaveVisible(boolean saveVisible) {
        this.saveVisible = saveVisible;
    }
}
