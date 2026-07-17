package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public class CustomLogicCFModal extends KpiModal implements Constants {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DataListBox fields;
    private DataListBox values;
    private final String formId;
    private final String categoryName;
    private ArrayList<SelectItem> customFields;
    private final SelectItem selectedField;
    private final String selectedValue;

    public CustomLogicCFModal(String formId, String categoryName, SelectItem selectedField, String selectedValue) {
        this.formId = formId;
        this.categoryName = categoryName;
        this.selectedField = selectedField;
        this.selectedValue = selectedValue;
        init();
    }

    private void init() {
        setTitle(wfmStrings.customLogic());
        AllInOneService.App.get().getCustomFieldsForCustomLogic(formId, categoryName, new AsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                customFields = result;
                initFields();
            }
        });
    }

    private void initFields() {
        fields = new DataListBox();
        values = new DataListBox();

        if (customFields != null) {
            fields.setItems(customFields.toArray(new SelectItem[]{}));
        }
        if (selectedField != null) {
            fields.setSelected(selectedField);
            values.setItems(selectedField.getRelatedItems());
        }
        if (selectedValue != null) {
            values.setSelectedByValue(selectedValue);
        }
        fields.addValueChangeHandler(event -> {
            if (fields.getSelectedItem() != null && fields.getSelectedItem().getRelatedItems() != null) {
                values.setItems(fields.getSelectedItem().getRelatedItems());
            } else {
                values.setItems(null);
            }
        });

        add(new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.field(), fields)),
                new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.value(), values))));

        addButton(new WfmButton2(wfmStrings.cancel(), event -> {
            fields.setSelectedItem(null);
            values.setSelectedItem(null);
            close();
        }));
        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> close()));
    }

    public SelectItem getSelectedField() {
        return fields.getSelectedItem();
    }

    public SelectItem getSelectedValue() {
        return values.getSelectedItem();
    }
}
