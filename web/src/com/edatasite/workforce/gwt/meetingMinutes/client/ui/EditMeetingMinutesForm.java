package com.edatasite.workforce.gwt.meetingMinutes.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 7/30/12
 * Time: 12:23 PM
 */
public class EditMeetingMinutesForm extends AddMeetingMinutesView {

    public EditMeetingMinutesForm(Integer objectID) {
        super("edit", wfmStrings.edit(), objectID);
    }

    @Override
    protected void addButtons() {
        super.addButtons();
    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return super.getFormType();
    }

    @Override
    protected Widget onInitialize() {
        if (container != null) {
            container.setDescription(wfmStrings.edit());
        }
        return super.onInitialize();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}