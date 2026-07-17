package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceTerminal;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;

public class AttendanceTerminalSummaryView extends CustomForm implements Constants, Colapse {
    private final Integer objectId;
    private static final String debugId = "attendanceTerminal_summary_view_";

    private HTML uuid;
    private HTML branch;
    private HTML dynamic;
    private HTML location;
    private KpiDataGrid<UserFingerPrintDeviceItem> dataGrid;

    public static final ProvidesKey<UserFingerPrintDeviceItem> KEY_PROVIDER =
            item -> item == null ? null : item.getUserId();

    public AttendanceTerminalSummaryView(Integer objectId) {
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
                branch.setText(result.getCompanyBranchName());
                uuid.setText(result.getCompanyUniqueID());
                dynamic.setText(result.getDynamicStatus() ? wfmStrings.yes() : wfmStrings.no());
                if (result.getLocation() != null) {
                    location.setText(result.getLocation().getName());
                }
                loadEnrolledEmployees(result.getCompanyUniqueID());
            }
        });
    }

    private void loadEnrolledEmployees(String deviceId) {
        HrmsService.App.get().getUserFingerprintDevicesByDeviceId(deviceId,
                new AbstractAsyncCallback<ArrayList<UserFingerPrintDeviceItem>>() {
                    @Override
                    public void success(ArrayList<UserFingerPrintDeviceItem> result) {
                        dataGrid.supplyProvider(result);
                        dataGrid.refresh();
                        addField(CustomFormConstants.ASSIGNEES, dataGrid, wfmStrings.assignedEmployees());
                    }
                });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ATTENDANCE_TERMINAL_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return "";
    }

    private void initialize() {
        uuid = new HTML();
        uuid.ensureDebugId(debugId + "uuid");

        branch = new HTML();
        branch.ensureDebugId(debugId + "branch");

        dynamic = new HTML();
        dynamic.ensureDebugId(debugId + "dynamic");

        location = new HTML();
        location.ensureDebugId(debugId + "location");

        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        initTableColumns();

        addField("UUID", uuid, wfmStrings.id());
        addField("BRANCH", branch, wfmStrings.branch());
        addField("DYNAMIC", dynamic, wfmStrings.dynamicType());
        addField("LOCATION", location, wfmStrings.location());
        addTitleField(CustomFormConstants.ASSIGNEE, wfmStrings.assignedEmployees());
        show();
    }

    private void initTableColumns() {
        Column<UserFingerPrintDeviceItem, String> nameColumn = new Column<UserFingerPrintDeviceItem, String>(new TextCell()) {
            @Override
            public String getValue(UserFingerPrintDeviceItem item) {
                return item.getUserName();
            }
        };
        dataGrid.addColumn(nameColumn, wfmStrings.employee());
        dataGrid.setColumnWidth(nameColumn, 50, Style.Unit.PCT);

        Column<UserFingerPrintDeviceItem, String> fpColumn = new Column<UserFingerPrintDeviceItem, String>(new TextCell()) {
            @Override
            public String getValue(UserFingerPrintDeviceItem item) {
                return item.getFingerPrintId();
            }
        };
        dataGrid.addColumn(fpColumn, "Fingerprint ID");
        dataGrid.setColumnWidth(fpColumn, 50, Style.Unit.PCT);
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
