package com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions;

import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAction;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowActionItem;
import com.google.gwt.user.client.ui.FlexTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by shohruh on 26-Mar-17.
 */
public abstract class AbstractWorkflowAction extends FlexTable implements Constants.WorkflowActionConstants.Type {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static Localize localize = new Localize();

    protected WorkflowAction action;
    protected Map<Integer, WorkflowActionItem> map;
    protected Map<Integer, ModelField> fieldsMap;
    LinkedList<WorkflowActionItemWidget> listOfWidgets = new LinkedList<>();

    public abstract boolean validate();

    public abstract String getActionName();

    public WorkflowAction getRPC() {
        action.setName(getActionName());
        List<WorkflowActionItem> items = new ArrayList<>();
        for (WorkflowActionItemWidget fieldWidget: listOfWidgets) {
            WorkflowActionItem item = fieldWidget.getRPC();
            if (item.getFieldId() != null) {
                item.setFieldIdString(fieldsMap.get(item.getFieldId()).getField_ID());
            }
            items.add(item);
        }
        action.setItems(items);
        return action;
    }
    
    public void addWidget(String label, SelectItem[] fields, int fieldType, WorkflowActionItem actionItem, int fieldId, boolean required, String... params) {
        listOfWidgets.add(new WorkflowActionItemWidget(this, label, fields, fieldType, actionItem, fieldId,required, params));
    }

    protected static SelectItem[] getColumnsAsReferenceItems(List<ModelField> fields) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields != null && fields.size() > 0) {
            for (ModelField field : fields) {
                String name = getLocalizedLabel(field);
                ReferenceItem referenceItem = field.asReferenceItem();
                referenceItem.setName(name != null ? name : referenceItem.getName());
                result.add(referenceItem);
            }
        }
        return result.toArray(new SelectItem[]{});
    }

    protected static String getLocalizedLabel(ModelField modelField) {
        String name = "";
        String localized = localize.localizeByFieldID(modelField.getForm_ID(), modelField.getField_ID());
        if (localized != null) {
            name = localized;
        } else if (modelField.getField_ID().contains("string_value") || modelField.getField_ID().contains("double_value") || modelField.getField_ID().contains("date_value")) {
            name = modelField.getLabel();
        } else {
            if (modelField.getDynamicLabel() != null && !modelField.getDynamicLabel().equals("")) {
                name = modelField.getDynamicLabel();
            } else {
                name = modelField.getField_ID();
            }
        }
        return name;
    }
}
