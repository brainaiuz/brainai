package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 9, 2010
 * Time: 7:03:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseActionsView extends View implements Constants, CommandConstants {
    interface DatabaseActionsViewUiBinder extends UiBinder<HTMLPanel, DatabaseActionsView> {
    }

    private static DatabaseActionsViewUiBinder ourUiBinder = GWT.create(DatabaseActionsViewUiBinder.class);
    protected static final BackendStrings backendStrings = BackendStrings.App.get();

    @UiField
    HTMLPanel panel;
    @UiField
    Span databasePatch;
    @UiField
    Label schemaLabel;
    @UiField
    HTMLPanel schemaPanel;
    @UiField
    Label excludeSchemaLabel;
    @UiField
    TextBox excludeSchema;
    @UiField
    KpiTextArea queryBox;
    @UiField
    HTMLPanel applyPatchPanel;
    @UiField
    Span hostSettings;
    @UiField
    Span hostSettingsInfo;

    @UiField
    HTMLPanel schemaCreatePanel;
    @UiField
    Span schemaCreateSettings;

    @UiField
    HTMLPanel clearHostSettingsPanel;
    @UiField
    Span updateAttendance;
    @UiField
    Label startDateLabel;
    @UiField
    DatePicker startDate;
    @UiField
    Label dueDateLabel;
    @UiField
    DatePicker dueDate;
    @UiField
    Label schema2Label;
    @UiField
    HTMLPanel schema2Panel;
    @UiField
    Label employeeLabel;
    @UiField
    DataListBox employee;
    @UiField
    HTMLPanel updateAttendancePanel;
    @UiField
    Span schemaUpdate;
    @UiField
    HTMLPanel schemaUpdatePanel;
    @UiField
    Label schema3Label;
    @UiField
    HTMLPanel schema3Panel;

    private SchemaLookUp schemaLookUp;
    private SchemaLookUp schemaLookUp2;
    private SchemaLookUp schemaLookUp3;

    public DatabaseActionsView() {
        super("applydatabasepatchadd", "Database Actions");
        ourUiBinder.createAndBindUi(this);
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        add(panel);
        return null;
    }

    public void initialize() {
        databasePatch.setText("Database SQL Patch");
        schemaLabel.setText(backendStrings.schema());
        excludeSchemaLabel.setText("Excluded schemas");
        hostSettings.setText("Clear Host Settings");
        hostSettingsInfo.setText("Clear Host based settings, domain settings, mail param settings from current instance");
        updateAttendance.setText(backendStrings.updateAttendace());
        schemaUpdate.setText("Schema Update");
        schemaCreateSettings.setText(wfmStrings.createSchema());
        startDateLabel.setText(wfmStrings.startDate());
        dueDateLabel.setText(wfmStrings.dueDate());
        schema2Label.setText(backendStrings.schema());
        schema3Label.setText(backendStrings.schema());
        employeeLabel.setText(wfmStrings.employee());

        schemaLookUp = new SchemaLookUp();
        schemaPanel.add(schemaLookUp);

        WfmButton2 applyButton = new WfmButton2("Apply Patch", WfmButton2.BTN_PRIMARY + " btn-block");
        applyButton.addClickHandler(clickEvent -> {
            if (schemaLookUp.getSelectedItem() == null) {
                Info.warn("Choose schema");
                return;
            }
            if (!Validation.validateTextAreaRequired(queryBox)) {
                Info.warn("Query is empty");
                return;
            }
            applyButton.setEnabled(false);
            String schemaName = schemaLookUp.getSelectedItemID() + "";
            if (schemaLookUp.getSelectedItemID() == -1) {
                schemaName = "freeSchemas";
            }
            String excludedSchemas = !Utils.isNullOrEmpty(excludeSchema.getText()) ? excludeSchema.getText() : null;
            String query = queryBox.getText().trim();
            LoadingPanel.loading(true);
            BackendService.App.get().applyPatch(schemaName, excludedSchemas, query, new AbstractAsyncCallback<String>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    applyButton.setEnabled(true);
                }

                @Override
                public void success(final String result) {
                    LoadingPanel.loading(false);
                    applyButton.setEnabled(true);
                    if (result != null && result.trim().equals("")) {
                        Window.alert("Patch applied successfully.");
                    } else {
                        Info.warn("Error during the patch.");
                        ScrollPanel sp = new ScrollPanel();
                        sp.add(new Label(result));
                        Window.alert("Error during the patch.");
                    }
                }
            });
        });
        applyPatchPanel.add(applyButton);

        WfmButton2 clearHostButton = new WfmButton2("Clear Host Settings", WfmButton2.BTN_PRIMARY + " btn-block");
        clearHostButton.addClickHandler(clickEvent -> {
            clearHostButton.setEnabled(false);
            LoadingPanel.loading(true);
            BackendService.App.get().clearHostSettings(new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    clearHostButton.setEnabled(true);
                    Info.warn("Error occured.");
                }

                @Override
                public void success(Void result) {
                    LoadingPanel.loading(false);
                    clearHostButton.setEnabled(true);
                    Info.show("Successfully Clear host setting cache.");
                }
            });
        });
        clearHostSettingsPanel.add(clearHostButton);

        schemaLookUp2 = new SchemaLookUp();
        schema2Panel.add(schemaLookUp2);

        schemaLookUp3 = new SchemaLookUp();
        schema3Panel.add(schemaLookUp3);

        schemaLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> getEmployees());
        schemaLookUp2.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> getEmployeesForSchemaLookUp2());

        WfmButton2 updateAttendanceButton = new WfmButton2("Update Attendance Raw Data", WfmButton2.BTN_PRIMARY + " btn-block");
        updateAttendanceButton.addClickHandler(clickEvent -> {
            boolean invalid = false;
            if (!Validation.validateDate(startDate)) {
                invalid = true;
            }
            if (!Validation.validateDate(dueDate)) {
                invalid = true;
            }
            if (schemaLookUp2.getSelectedItemID() == null) {
                invalid = true;
            }
            if (invalid) {
                Info.warn(wfmStrings.sureEnteredAllData());
                return;
            } else if (startDate.getDate().after(dueDate.getDate())) {
                Info.warn("Please select correct date");
                return;
            }
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCompanyID(schemaLookUp2.getSelectedItemID());
            fp.setStartDateNC(Utils.getStartDateNCForFilter(startDate.getDate()));
            fp.setEndDateNC(Utils.getEndDateNCForFilter(dueDate.getDate()));
            fp.setEmployeeId(employee.getSelectedId());
            LoadingPanel.loading(true);
            updateAttendanceButton.setEnabled(false);
            BackendService.App.get().createAttendaceRawDataRecords(fp, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    updateAttendanceButton.setEnabled(true);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(Void aVoid) {
                    LoadingPanel.loading(false);
                    updateAttendanceButton.setEnabled(true);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.item()));
                }
            });
        });
        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com")) {
            updateAttendancePanel.add(updateAttendanceButton);
        }

        WfmButton2 schemaUpdateButton = new WfmButton2("Schema Update Button", WfmButton2.BTN_PRIMARY + " btn-block");
        schemaUpdateButton.addClickHandler(clickEvent -> {
            boolean invalid = false;
            if (schemaLookUp3.getSelectedItemID() == null) {
                invalid = true;
            }
            if (invalid) {
                Info.warn(wfmStrings.sureEnteredAllData());
                return;
            }
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCompanyID(schemaLookUp2.getSelectedItemID());
            String args[] = {schemaLookUp3.getSelectedItemID()+""};
            LoadingPanel.loading(true);
            schemaUpdateButton.setEnabled(false);
            BackendService.App.get().runSchemaUpdate(args, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    schemaUpdateButton.setEnabled(true);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(Void aVoid) {
                    LoadingPanel.loading(false);
                    schemaUpdateButton.setEnabled(true);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.item()));
                }
            });
        });
        schemaUpdatePanel.add(schemaUpdateButton);

        TextBox textBox = new TextBox();
        Validation.addNumericKeyboardListener(textBox);
        GColumn column = new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.count(), textBox));

        WfmButton2 button2 = new WfmButton2("Create schema", WfmButton2.BTN_PRIMARY);
        button2.addClickHandler(click -> {
            button2.setEnabled(false);
            if (!Validation.validateTextBoxRequired(textBox)) {
                Info.warn("Schema count is empty");
                button2.setEnabled(true);
                return;
            }
            if (textBox.getText() != null && textBox.getText().matches(Constants.REGEX_INTEGER)) {
                Integer count = Integer.valueOf(textBox.getText());
                if (count > 50) {
                    Info.warn("Max 50ta");
                    button2.setEnabled(true);
                    return;
                } else {
                    LoadingPanel.loading(true);
                    BackendService.App.get().createTemplateSchema(count, new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            button2.setEnabled(true);
                            Info.warn("yedi");
                        }

                        @Override
                        public void onSuccess(Integer o) {
                            LoadingPanel.loading(false);
                            button2.setEnabled(true);
                            if (o == null) {
                                Info.show("yedi");
                            } else {
                                Info.show(o + " ta schema yaratildi");
                            }
                        }
                    });
                }
            }

        });
        GColumn column2 = new GColumn(GColumnEnum.COL_6, new FormGroup("", button2));

        MaterialPanel gridRow = new MaterialPanel("grid-row");
        gridRow.add(column);
        gridRow.add(column2);
        schemaCreatePanel.add(gridRow);

    }

    private void getEmployees() {
        employee.clear();
        if (schemaLookUp.getSelectedItemID() == null || schemaLookUp.getSelectedItemID() == 0 || schemaLookUp.getSelectedItemID() == -1) {
            return;
        }
        LoadingPanel.loading(true);
        ReportService.App.get().getEmployeesList(schemaLookUp.getSelectedItemID(), new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                LoadingPanel.loading(false);
                employee.setSelectedNullLabel();
                employee.setItems(result);
            }
        });
    }

    private void getEmployeesForSchemaLookUp2() {
        employee.clear();
        if (schemaLookUp2.getSelectedItemID() == null || schemaLookUp2.getSelectedItemID() == 0 || schemaLookUp2.getSelectedItemID() == -1) {
            return;
        }
        LoadingPanel.loading(true);
        ReportService.App.get().getEmployeesList(schemaLookUp2.getSelectedItemID(), new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                LoadingPanel.loading(false);
                employee.setSelectedNullLabel();
                employee.setItems(result);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
