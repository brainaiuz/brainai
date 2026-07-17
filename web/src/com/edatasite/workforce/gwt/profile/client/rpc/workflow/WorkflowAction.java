package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.*;

/**
 * Created by shohruh on 23-Mar-17.
 */
public class WorkflowAction implements IsSerializable {
    //Certificate fields
    public final static String BOX_1 = "$$input:textbox1$$";
    public final static String BOX_2 = "$$input:textbox2$$";
    public final static String BOX_3 = "$$input:textbox3$$";
    public final static String BOX_4 = "$$input:textbox4$$";
    public final static String BOX_5 = "$$input:textbox5$$";
    public final static String BOX_6 = "$$input:textbox6$$";
    public final static String BOX_7 = "$$input:textbox7$$";
    public final static String BOX_8 = "$$input:textbox8$$";
    public final static String BOX_9 = "$$input:textbox9$$";
    public final static String BOX_10 = "$$input:textbox10$$";
    public final static String BOX_11 = "$$input:textbox11$$";
    public final static String BOX_12 = "$$input:textbox12$$";
    public final static String BOX_13 = "$$input:textbox13$$";
    public final static String BOX_14 = "$$input:textbox14$$";
    public final static String BOX_15 = "$$input:textbox15$$";
    public final static String BOX_16 = "$$input:textbox16$$";
    public final static String BOX_17 = "$$input:textbox17$$";
    public final static String BOX_18 = "$$input:textbox18$$";
    public final static String TEXT_AREA_1 = "$$input:textarea1$$";
    public final static String TEXT_AREA_2 = "$$input:textarea2$$";
    public final static String TEXT_AREA_3 = "$$input:textarea3$$";
    public final static String TEXT_AREA_4 = "$$input:textarea4$$";
    public final static String TEXT_AREA_5 = "$$input:textarea5$$";
    public final static String TEXT_AREA_6 = "$$input:textarea6$$";
    public final static String TEXT_AREA_7 = "$$input:textarea7$$";
    public final static String TEXT_AREA_8 = "$$input:textarea8$$";

    private Integer id;
    private String name;
    private Integer actionType;
    private Date createdDate;
    private String formId;
    private Integer workflowId;
    private List<WorkflowActionItem> items;
    private List<ModelField> fields;

    private Map<Integer, WorkflowActionItem> itemsMap;
    private Map<Integer, ModelField> fieldsMap;

    public WorkflowAction() {
    }

    public static ArrayList<Integer> getIds(Set<WorkflowAction> selectedItems) {
        ArrayList<Integer> result = new ArrayList<>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (WorkflowAction item : selectedItems) {
                if (item.getId() != null) {
                    result.add(item.getId());
                }
            }
        }
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public List<WorkflowActionItem> getItems() {
        if (items == null) items = new ArrayList<>();
        return items;
    }

    public void setItems(List<WorkflowActionItem> items) {
        this.items = items;
    }

    public List<ModelField> getFields() {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        return fields;
    }

    public void setFields(List<ModelField> fields) {
        this.fields = fields;
    }

    public void addField(ModelField field) {
        getFields().add(field);
    }

    public void addItem(WorkflowActionItem item) {
        getItems().add(item);
    }

    public Map<Integer, WorkflowActionItem> getItemsAsMap() {
        if (itemsMap == null) {
            itemsMap = new HashMap<>();
            for (WorkflowActionItem item : getItems()) {
                itemsMap.put(item.getMappedId(), item);
            }
        }
        return itemsMap;
    }

    public Map<Integer, ModelField> getFieldsAsMap() {
        if (fieldsMap == null) {
            fieldsMap = new HashMap<>();
            for (ModelField field : getFields()) {
                fieldsMap.put(field.getObjectID(), field);
            }
        }
        return fieldsMap;
    }

    public static String[] getCertificateFields() {
        return new String[]{BOX_1, BOX_2, BOX_3, BOX_4, BOX_5, BOX_6, BOX_7, BOX_8, BOX_9, BOX_10, BOX_11, BOX_12, BOX_13, BOX_14, BOX_15, BOX_16, BOX_17, BOX_18,
                TEXT_AREA_1, TEXT_AREA_2, TEXT_AREA_3, TEXT_AREA_4, TEXT_AREA_5, TEXT_AREA_6, TEXT_AREA_7, TEXT_AREA_8};
    }
}
