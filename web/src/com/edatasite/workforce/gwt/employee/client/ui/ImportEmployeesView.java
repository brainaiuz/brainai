package com.edatasite.workforce.gwt.employee.client.ui;


import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.GoogleMarketPlaceUsersView;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 10, 2010
 * Time: 9:34:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportEmployeesView extends View implements Constants, Errors, Colapse {

    public ImportEmployeesView(String from) {
        super("add", from);
    }

    protected Widget onInitialize() {
        add(new GoogleMarketPlaceUsersView(false));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, ImportEmployeesView.this, (sender, args) -> {
//                if (args instanceof GoogleMarketPlaceUsersView) {
            closeTab(/*"importemployees|add/add"*/);
//                }
        });
        return null;
    }

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
