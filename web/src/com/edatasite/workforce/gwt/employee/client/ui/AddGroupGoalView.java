package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class AddGroupGoalView extends View implements Constants, Errors, Colapse {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private Integer objectId;


    public AddGroupGoalView(Integer groupId) {
        super("add", hrmsStrings.addGroupGoal());
        this.objectId = groupId;
    }

    public AddGroupGoalView(String groupString, Integer groupId) {
        super("summary", hrmsStrings.addGroupGoal());
        this.objectId = groupId;
    }

    protected Widget onInitialize() {
        AddGroupGoalUiBinder groupGoalUiBinder = new AddGroupGoalUiBinder(objectId);
        groupGoalUiBinder.init();
        add(groupGoalUiBinder);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GROUP_GOAL_CLOSE, AddGroupGoalView.this, (sender, args) -> closeTab());
        return null;
    }


    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
