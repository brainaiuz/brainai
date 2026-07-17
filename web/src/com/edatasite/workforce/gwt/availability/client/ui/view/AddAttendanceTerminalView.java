package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialTextBox;

public class AddAttendanceTerminalView extends CustomForm implements Constants, Colapse {
    private static final String debugId = "attendanceTerminal_add_view_";
    private MaterialTextBox uuid;
    private MaterialTextBox branch;
    private MaterialCheckBox dynamic;
    private LocationLookUpWithCode location;

    public AddAttendanceTerminalView() {
        super("add", wfmStrings.add());
        Utils.log("AddAttendanceTerminalView constructor");
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

    @Override
    protected void getDataToFillFields() {
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
        location.ensureDebugId(debugId + "location");

        addField("UUID", uuid, wfmStrings.id());
        addField("BRANCH", branch, wfmStrings.branch());
        addField("DYNAMIC", dynamic, wfmStrings.dynamicType());
        addField("LOCATION", location, wfmStrings.location());
        show();
    }

    private void save() {
        clearErrorStyle();

        if (!validate()) {
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
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ATTENDANCE_TERMINAL_ADD, result, AddAttendanceTerminalView.this);
                enableButton(true);
                closeTab();
            }
        });
    }

    private AttendanceTerminal buildCompanyDomainForSave() {
        AttendanceTerminal domain = new AttendanceTerminal();

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
