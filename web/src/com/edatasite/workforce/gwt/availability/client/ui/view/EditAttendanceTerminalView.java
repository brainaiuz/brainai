package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceTerminal;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialTextBox;

public class EditAttendanceTerminalView extends CustomForm implements Constants, Colapse {
    private final Integer objectId;
    private static final String debugId = "attendanceTerminal_summary_view_";
    private MaterialTextBox uuid;
    private MaterialTextBox branch;
    private MaterialCheckBox dynamic;
    private LocationLookUpWithCode location;

    public EditAttendanceTerminalView(Integer objectId) {
        super("summary", wfmStrings.summaryView());
        this.objectId = objectId;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    protected void addButtons() {
        MaterialButton save = new MaterialButton(wfmStrings.save());
        save.addStyleName(Constants.BTN_PRIMARY);
        save.addClickHandler(event -> save());

        addButton(save);
    }

    private void save() {
        clearErrorStyle();

        if (!validate()) {
            Info.warn(wfmStrings.fillAllRequiredFields());
            return;
        }

        enableButton(false);

        AttendanceTerminal dto = buildCompanyDomainForSave();

        CommonService.App.get().saveAttendanceTerminal(dto, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                enableButton(true);
                super.failure(throwable);
            }

            @Override
            public void success(Integer result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ATTENDANCE_TERMINAL_EDIT, result, EditAttendanceTerminalView.this);
                enableButton(true);
                closeTab();
                Info.show("Success " + result);
            }
        });
    }

    private AttendanceTerminal buildCompanyDomainForSave() {
        AttendanceTerminal domain = new AttendanceTerminal();

        domain.setId(objectId);
        if (uuid != null) {
            domain.setCompanyUniqueID(uuid.getValue());
        }
        if (branch != null) {
            domain.setCompanyBranchName(branch.getValue());
        }
        if (dynamic != null) {
            domain.setDynamicStatus(dynamic.getValue());
        }
        if (location != null) {
            domain.setLocationId(location.getSelectedItemID());
        }

        return domain;
    }

    private boolean validate() {
        int errors = 0;

        if (uuid == null || uuid.getValue() == null || uuid.getValue().trim().isEmpty()) {
            errors += markAsError(uuid, true);
        }

        if (branch == null || branch.getValue() == null || branch.getValue().trim().isEmpty()) {
            errors += markAsError(branch, true);
        }

        return errors == 0;
    }

    @Override
    protected void getDataToFillFields() {
        CommonService.App.get().getAttendanceTerminal(objectId, new AbstractAsyncCallback<AttendanceTerminal>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(AttendanceTerminal result) {
                super.success(result);
                branch.setValue(result.getCompanyBranchName());
                uuid.setValue(result.getCompanyUniqueID());
                dynamic.setValue(result.getDynamicStatus());
                location.setSelected(result.getLocation());
                if (result.getExternalId() != null) {
                    uuid.setReadOnly(true);
                    branch.setReadOnly(true);
                    dynamic.setEnabled(false);
                    location.setEnabled(false);
                    enableButton(false);
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ATTENDANCE_TERMINAL_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return "";
    }

    private void initialize() {
        uuid = new MaterialTextBox();
        uuid.ensureDebugId(debugId + "uuid");

        branch = new MaterialTextBox();
        branch.ensureDebugId(debugId + "branch");

        dynamic = new MaterialCheckBox();
        dynamic.ensureDebugId(debugId + "dynamic");

        location = new LocationLookUpWithCode();
        location.setEnsureDebugId(debugId + "location");

        addField("UUID", uuid, wfmStrings.id());
        addField("BRANCH", branch, wfmStrings.branch());
        addField("DYNAMIC", dynamic, wfmStrings.dynamicType());
        addField("LOCATION", location, wfmStrings.location());
        show();
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
