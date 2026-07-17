package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowInvoiceField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by Azazello on 10/7/16.
 */
public class WorkflowInvoiceWidget extends HorizontalPanel implements Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    private SelectItem[] addSubtractItems = new SelectItem[]{new SelectItem(1, wfmStrings.add(), ADD),
                                                             new SelectItem(2, wfmStrings.subtract(), SUBTRACT)};
    private SelectItem[] demandOnItems = new SelectItem[]{new SelectItem(1, wfmStrings.totalAmount(), TOTAL_AMOUNT),
            new SelectItem(2, wfmStrings.clientBalance(), CLIENT_BALANCE)};
    private ModelField field;
    private TextBox value;
    private KpiCheckBox percentage;
    private KpiCheckBox demandOn;
    private DataListBox addSubtract;
    private DataListBox customFieldID;

    public WorkflowInvoiceWidget(ModelField field) {
        super();
        this.field = field;
        initialize();
    }

    private void initialize() {
        addSubtract = new DataListBox();
        addSubtract.setWidth("150px");
        addSubtract.setItems(addSubtractItems);
        value = new TextBox();
        value.setWidth("200px");
        Validation.addNumericKeyboardListener(value);
        percentage = new KpiCheckBox();
        percentage.setText("Percentage");
        demandOn = new KpiCheckBox();
        demandOn.setText("Demand On");
        demandOn.addValueChangeHandler(booleanValueChangeEvent -> {
            if(booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()){
                customFieldID.setVisible(true);
            } else {
                customFieldID.setSelectedNullLabel();
                customFieldID.setVisible(false);
            }
        });
        customFieldID = new DataListBox();
        customFieldID.setWidth("200px");
        customFieldID.setItems(demandOnItems);
        customFieldID.setVisible(false);
        add(addSubtract);
        add(value);
        add(percentage);
        add(demandOn);
        add(customFieldID);
        setSpacing(10);
        setWidth("100%");
    }

    public WorkflowInvoiceField getInvoiceField(String totalAmount) {
        WorkflowInvoiceField item = new WorkflowInvoiceField();
        item.setField(field);
        if (addSubtract.getSelectedItem() != null) {
            item.setAction(addSubtract.getSelectedItem().getReferenceCode());
        }
        item.setValue(value.getText());
        item.setPercentage(percentage.getValue() != null && percentage.getValue());
        item.setDemandOn(demandOn.getValue() != null && demandOn.getValue());
        if(customFieldID.getSelectedItem() != null){
            item.setCustomFieldID(customFieldID.getSelectedItem().getReferenceCode());
        }
        return item;
    }

    public void setField(ModelField field) {
        this.field = field;
    }

    public void fill(WorkflowInvoiceField item){
        if(item.getAction() != null){
            addSubtract.setSelectedByCode(item.getAction());
        }
        value.setText(item.getValue());
        percentage.setValue(item.isPercentage());
        if (item.isDemandOn()) {
            demandOn.setValue(true);
            customFieldID.setVisible(true);
            if(item.getCustomFieldID() != null){
                customFieldID.setSelectedByCode(item.getCustomFieldID());
            }
        }
    }
}
