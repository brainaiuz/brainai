package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class TerminalAttendanceEmployeeTab extends BaseListView {

    private Integer employeeId;
    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    public TerminalAttendanceEmployeeTab(Integer employeeId) {
        super("terminalAttendanceEmployeeTab", hrmsStrings.terminalAttendance());
        this.employeeId = employeeId;
    }

    @Override
    protected Widget onInitialize() {
            add(new TerminalAttendanceView(employeeId));
        return this;
    }



    @Override
    public String getIconStyle() {
        return "";
    }

    @Override
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
