package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;


public class EditTimeslotView extends AddTimeslotView {

    public EditTimeslotView(Integer int_timeSlotID) {
        super(int_timeSlotID, int_timeSlotID == 1);//usually default timeslot's id is 1, therefore no need to check it in the server
    }

    @Override
    public String getIconStyle() {
        return null;
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
    protected String getWikiCode() {
        return super.getWikiCode();
    }

    @Override
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
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