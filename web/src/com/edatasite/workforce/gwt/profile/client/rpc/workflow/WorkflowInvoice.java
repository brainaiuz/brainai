package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Azazello on 10/6/16.
 */
public class WorkflowInvoice implements IsSerializable {
    private Integer objectID;
    private Integer workflowID;
    private ArrayList<WorkflowInvoiceField> invoiceFields;
    private ArrayList<ModelField> fields;

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

    public ArrayList<WorkflowInvoiceField> getInvoiceFields() {
        if(invoiceFields == null){
            invoiceFields = new ArrayList<>();
        }
        return invoiceFields;
    }

    public ArrayList<ModelField> getFields() {
        if(fields == null){
            fields = new ArrayList<>();
        }
        return fields;
    }

    public void setFields(ArrayList<ModelField> fields) {
        this.fields = fields;
    }

    public Map<String, ModelField> getFieldsMap(){
        Map<String, ModelField> fieldsMap = new HashMap<>();
        for(ModelField f : getFields()){
            if (!fieldsMap.containsKey(f.getField_ID())) {
                fieldsMap.put(f.getField_ID(), f);
            }
        }
        return fieldsMap;
    }
}
