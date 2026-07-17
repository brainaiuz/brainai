package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;


/**
 * Created by Hayot on 3/15/14.
 */
public class WorkflowUpdateField implements IsSerializable {
    private Integer objectID;
    private Integer workflowID;
    private String formID;
    private String customFormID;
    private String fieldID;
    private String value;
    private String label;
    private Date dateValue;
    private ArrayList<ModelField> fields;
    private SelectItem[] modules;
    private Integer conditionId;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getCustomFormID() {
        return customFormID;
    }

    public void setCustomFormID(String customFormID) {
        this.customFormID = customFormID;
    }

    public String getFieldID() {
        return fieldID;
    }

    public void setFieldID(String fieldID) {
        this.fieldID = fieldID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static ArrayList<Integer> getIDsOnly(Set<WorkflowUpdateField> selectedItems) {
        ArrayList<Integer> result = new ArrayList<>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (WorkflowUpdateField item : selectedItems) {
                if (item.getObjectID() != null) {
                    result.add(item.getObjectID());
                }
            }
        }
        return result;
    }

    public String getStringValue() {
        if (value != null && value.contains("@")) {
            String[] s = value.split("@");
            if (s.length == 1) {
                if (s[0] != null && !"".equals(s[0]) && !s[0].matches("[0-9]+")) {
                    return s[0];
                }
            } else {
                if (s[0] != null && !"".equals(s[0]) && s[0].matches("[0-9]+")) {
                    return s[1];
                } else {
                    return "Custom value";
                }
            }
        }
        return value;
    }

    public static Integer extractIDFromValue(String source, String value) {
        if (value != null && value.contains("@")) {
            String[] s = value.split("@");
            if (s.length > 0) {
                if (s[0] != null && !"".equals(s[0]) && s[0].matches("[0-9]+")) {
                    return Integer.parseInt(s[0]);
                }
            }
        }
        return null;
    }

    public ArrayList<ModelField> getFields() {
        return fields;
    }

    public void setFields(ArrayList<ModelField> fields) {
        this.fields = fields;
    }

    public SelectItem[] getModules() {
        return modules;
    }

    public void setModules(SelectItem[] modules) {
        this.modules = modules;
    }

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }
}
