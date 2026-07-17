package com.edatasite.workforce.gwt.core.client.rpc.form;

import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.form.DynamicSectionsRpc;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * User: Hayot
 * Date: 02/08/2014
 * Time: 10:51 AM
 */
public class ModelForm implements IsSerializable, LayoutInterface {

    private Integer objectID;
    private String formID;
    boolean active;
    boolean stepForm;
    boolean customForm;
    boolean certificateForm;
    private String title;
    private String viewName;
    private String layout;
    private ArrayList<ModelField> fields = new ArrayList<>();
    private ArrayList<String> requiredCodes = new ArrayList<>();
    private boolean isCustom = false;
    private SelectItem[] attributes;
    private String[] additionalFields;
    private LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>> columnMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedList<CustomizeFormItem>> gridColumnMap = new LinkedHashMap<>();
    private LinkedHashMap<String, DynamicSectionsRpc> sectionsRpcMap = new LinkedHashMap<>();
    private boolean collapse;
    private boolean isQuizForm;
    private boolean isanonymousForm;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ModelField> getFields() {
        return fields;
    }

    public void setFields(ArrayList<ModelField> fields) {
        this.fields = fields;
    }

    public boolean isStepForm() {
        return stepForm;
    }

    public void setStepForm(boolean stepForm) {
        this.stepForm = stepForm;
    }

    public boolean isCertificateForm() {
        return certificateForm;
    }

    public void setCertificateForm(boolean certificateForm) {
        this.certificateForm = certificateForm;
    }

    public void addField(ModelField field) {
        getFields().add(field);
    }

    public ArrayList<String> getRequiredCodes() {
        if (requiredCodes.size() == 0 && fields.size() > 0) {
            for (ModelField field : fields) {
                if (field.isMandatory()) {//required
                    requiredCodes.add(field.getField_ID());
                }
            }
        }
        return requiredCodes;
    }

    public boolean isButtonPanelDisabled() {
        return layout != null && layout.contains(CustomFormConstants.DISABLE_BUTTON_PANEL);
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getLayout() {
        return layout;
    }

    public boolean isCustom() {
        return isCustom || objectID == null;
    }

    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public ModelField getFieldByFieldID(String id) {
        if (getFields() != null && getFields().size() > 0) {
            for (ModelField field : getFields()) {
                if (field.getField_ID() != null && field.getField_ID().equals(id)) {
                    return field;
                }
            }
        }
        return null;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public SelectItem[] getAttributes() {
        return attributes;
    }

    public void setAttributes(SelectItem[] attributes) {
        this.attributes = attributes;
    }

    public String[] getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(String[] additionalFields) {
        this.additionalFields = additionalFields;
    }

    public LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>> columnMap) {
        this.columnMap = columnMap;
    }

    public LinkedHashMap<String, LinkedList<CustomizeFormItem>> getGridColumnMap() {
        return gridColumnMap;
    }

    public void setGridColumnMap(LinkedHashMap<String, LinkedList<CustomizeFormItem>> gridColumnMap) {
        this.gridColumnMap = gridColumnMap;
    }

    public LinkedHashMap<String, DynamicSectionsRpc> getSectionsRpcMap() {
        return sectionsRpcMap;
    }

    public void setSectionsRpcMap(LinkedHashMap<String, DynamicSectionsRpc> sectionsRpcMap) {
        this.sectionsRpcMap = sectionsRpcMap;
    }

    public boolean isCollapse() {
        return collapse;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    public boolean isCustomForm() {
        return customForm;
    }

    public void setCustomForm(boolean customForm) {
        this.customForm = customForm;
    }

    public boolean isQuizForm() {
        return isQuizForm;
    }

    public void setQuizForm(boolean quizForm) {
        isQuizForm = quizForm;
    }

    public boolean isAnonymousForm() {
        return isanonymousForm;
    }

    public void setAnonymousForm(boolean anonymousForm) {
        isanonymousForm = anonymousForm;
    }
}
