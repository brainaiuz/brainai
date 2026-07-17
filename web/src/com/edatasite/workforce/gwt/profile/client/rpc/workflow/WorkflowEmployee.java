package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by Azazello on 4/25/16.
 */
public class WorkflowEmployee implements IsSerializable {
    private Integer objectID;
    private Integer workflowID;
    private String workflowModule;
    private ArrayList<ModelField> fields;
    private HashMap<String, String> values;
    private String valuesAsString;
    private boolean workflowActionTimeBased;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;

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

    public String getWorkflowModule() {
        return workflowModule;
    }

    public void setWorkflowModule(String workflowModule) {
        this.workflowModule = workflowModule;
    }

    public HashMap<String, String> getValues() {
        if(values == null){
            values = new LinkedHashMap<>();
        }
        return values;
    }

    public void setValues(HashMap<String, String> values) {
        this.values = values;
    }

    public String getValuesAsString() {
        return valuesAsString;
    }

    public void setValuesAsString(String valuesAsString) {
        this.valuesAsString = valuesAsString;
    }

    public ArrayList<ModelField> getFields() {
        if(fields == null){
            fields = new ArrayList<>();
        }
        return fields;
    }

    public static ArrayList<Integer> getIDsOnly(Set<WorkflowEmployee> selectedItems) {
        ArrayList<Integer> result = new ArrayList<>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (WorkflowEmployee item : selectedItems) {
                if (item.getObjectID() != null) {
                    result.add(item.getObjectID());
                }
            }
        }
        return result;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }
}
