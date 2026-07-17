package com.edatasite.workforce.gwt.reportingsystem.client.ui.step;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;

/**
 * Created by Virus on 8/27/14.
 */
public class ReportingSinksContainer extends SinksContainer {
    public ReportingSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Integer id = null;
        String type = null;
        String uuid = null;
        Integer budgetId = null;
        String name = wfmStrings.reportingSystem();
        String specificReportName = null;
        if (params != null && params.length > 1) {
            type = params[1];
            id = Integer.valueOf(params[0]);
            if ("aireport".equals(type)) {
                uuid = params[2];
            }
            if (params.length > 2 && params[3] != null && !params[3].equals("null")) {
                budgetId = Integer.valueOf(params[3]);
            }
            if (params.length > 3 && params[4] != null) {
                specificReportName = Utils.decrypt(URL.decodeQueryString(params[4]));
            }
        }

        ReportingStepControlView stepControlView = null;
        if (uuid != null) {
            stepControlView = new ReportingStepControlView(type, uuid, name);
        } else if (budgetId != null) {
            stepControlView = new ReportingStepControlView(type, id, name, budgetId);
        } else {
            stepControlView = new ReportingStepControlView(type, id, name, specificReportName);
        }
        addView(stepControlView);
    }

    public void initializeAsync(final View view) {
        view.asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                LoadingPanel.loading(false);
                workarea.getViewById().remove(view.getName());
                Info.show(wfmStrings.networkErrorOccurred(), Info.Type.WARNING);
            }

            public void success(Widget widget) {
            }
        });
    }
}
