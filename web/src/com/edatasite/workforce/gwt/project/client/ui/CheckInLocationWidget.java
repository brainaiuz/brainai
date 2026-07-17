package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.project.CheckInLocationItem;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

public class CheckInLocationWidget extends Composite {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    public Integer objectId;
    public TextBox latitude;
    public TextBox longitude;
    public TextBox radius;
    private boolean isSummary = false;

    public CheckInLocationWidget() {
        initialize();
    }

    public CheckInLocationWidget(Integer objectId, String latitude, String longitude, String radius) {
        initialize();
        this.objectId = objectId;
        this.latitude.setText(latitude);
        this.longitude.setText(longitude);
        this.radius.setText(radius);
    }

    public CheckInLocationWidget(Integer objectId, String latitude, String longitude, String radius, boolean isSummary) {
        initialize();
        this.objectId = objectId;
        this.latitude.setText(latitude);
        this.longitude.setText(longitude);
        this.radius.setText(radius);
        this.isSummary = isSummary;
        enableFields();
    }

    private void initialize() {
        latitude = new TextBox();
        latitude.setPlaceHolder(wfmStrings.latitude());
        longitude = new TextBox();
        longitude.setPlaceHolder(wfmStrings.longitude());
        radius = new TextBox();
        radius.setPlaceHolder(wfmStrings.radius());

        Validation.addNumericKeyboardListener(latitude, 7);
        Validation.addNumericKeyboardListener(longitude, 7);
        Validation.addNumericKeyboardListener(radius, 0);
    }

    public CheckInLocationItem getCheckInLocationItem() {
        CheckInLocationItem item = new CheckInLocationItem();
        item.setId(objectId);
        item.setLatitude(latitude.getValue());
        item.setLongitude(longitude.getValue());
        item.setRadius(radius.getValue());
        return item;
    }

    private void enableFields() {
        latitude.setEnabled(!isSummary);
        longitude.setEnabled(!isSummary);
        radius.setEnabled(!isSummary);
    }


}
