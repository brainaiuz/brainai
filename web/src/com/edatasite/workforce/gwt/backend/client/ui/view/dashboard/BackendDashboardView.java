package com.edatasite.workforce.gwt.backend.client.ui.view.dashboard;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Abdulaziz on 22.04.2016.
 */
public class BackendDashboardView extends View implements Constants {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    public static final String BACKEND_DASHBOARD = "BACKEND_DASHBOARD";
    private String defaultbackendDashboard;
    private BackendDashboard backendDashboard;

    public BackendDashboardView() {
        super("backenddashboard", backendStrings.backendDashboard());
    }

    public BackendDashboardView(String backendDashboard) {
        this();
        this.defaultbackendDashboard = backendDashboard;
    }

    @Override
    public String getIconStyle() {
        return "backend backendListView";
    }

    @Override
    protected Widget onInitialize() {
        backendDashboard = new BackendDashboard();
        add(backendDashboard);
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
