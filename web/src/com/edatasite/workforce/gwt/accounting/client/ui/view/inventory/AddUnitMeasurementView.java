package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.UnitMeasurementItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 17, 2010
 * Time: 4:40:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddUnitMeasurementView extends KpiModal implements Constants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private TextBox nameTextBox;
    private TextArea descriptionTextArea;
    private WfmButton2 saveButton;

    private ObjectCommand providerCommand;
    private String addUnitMeasureListView = "add_unit_measure_list_view_";
    private Integer objectID;
    private UnitMeasurementItem measurementItem;

    public AddUnitMeasurementView(Integer objectID, ObjectCommand providerCommand) {
        this.objectID = objectID;
        this.providerCommand = providerCommand;
        setTitle(objectID != null ? accountingStrings.editMeasurement() : accountingStrings.addMeasurement());
        setWidth(400);
        init();
        loadData();
        open();
    }

    private void init() {
        nameTextBox = new TextBox();
        nameTextBox.ensureDebugId(addUnitMeasureListView + "nameTextBox");

        descriptionTextArea = new TextArea();
        descriptionTextArea.setWidth("100%");
        descriptionTextArea.setHeight("150px");
        descriptionTextArea.ensureDebugId(addUnitMeasureListView + "descriptionTextArea");

        addWidget(nameTextBox, wfmStrings.name());
        addWidget(descriptionTextArea, wfmStrings.description());

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveButton);
    }

    private void loadData() {
        if (objectID != null) {
            AccountingService.App.get().getUnitMeasurement(objectID, new AbstractAsyncCallback<UnitMeasurementItem>() {
                public void failure(Throwable caught) {

                }

                public void success(UnitMeasurementItem result) {
                    measurementItem = result;
                    nameTextBox.setText(measurementItem.getName());
                    descriptionTextArea.setText(measurementItem.getDescription());
                }
            });
        }
    }

    public void save() {
        if(!validate()){
            return;
        }
        if (measurementItem == null) {
            measurementItem = new UnitMeasurementItem();
        }
        measurementItem.setName(nameTextBox.getText());
        measurementItem.setDescription(descriptionTextArea.getText());
        saveButton.setEnabled(false);
        LoadingPanel.loading(true, AddUnitMeasurementView.this);
        AccountingService.App.get().saveUnitMeasurement(measurementItem, new AbstractAsyncCallback<SelectItem>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(true, AddUnitMeasurementView.this);
                saveButton.setEnabled(true);
            }

            public void success(SelectItem result) {
                LoadingPanel.loading(true, AddUnitMeasurementView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.measurement()), Info.Type.INFO);
                close();
                if (providerCommand != null) {
                    providerCommand.execute(result);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_UNITMEASUREMENT_SAVED, null, null);
            }
        });
    }

    public boolean validate() {
        if (!Validation.validateTextBoxRequired(nameTextBox)) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }
}
