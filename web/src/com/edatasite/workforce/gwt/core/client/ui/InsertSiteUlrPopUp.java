package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by Faxriddin on 7/15/2016.
 */
public class InsertSiteUlrPopUp extends KpiModal implements Constants {

    private String officedataType;

    interface InsertSiteUlrPopUpUiBinder extends UiBinder<HTMLPanel, InsertSiteUlrPopUp> {
    }

    private static InsertSiteUlrPopUpUiBinder ourUiBinder = GWT.create(InsertSiteUlrPopUpUiBinder.class);
    @UiField
    TextBox name;
    @UiField
    HTML error;

    private Button saveButton = new Button("Go");
    private Button cancelButton = new Button("Back");

    public InsertSiteUlrPopUp(String storageType) {
        super();
        setCloseButton(true);
        this.officedataType = storageType;
        addStyleName("bt_area");
        add(ourUiBinder.createAndBindUi(this));
        setSize("270px", "141px");
        addButton(cancelButton);
        addButton(saveButton);

        saveButton.addClickHandler(event -> {
            if (validate()) {
                saveButton.setEnabled(false);
                cancelButton.setEnabled(false);
                setGoogleCookies(officedataType);
                if (GWT.getHostPageBaseURL().endsWith("/")) {
                    Utils.redirect(GWT.getHostPageBaseURL() + "office365/auth/link?website_url=" + name.getText());
                } else {
                    Utils.redirect(GWT.getHostPageBaseURL() + "/office365/auth/link?website_url=" + name.getText());
                }
            }
        });
        cancelButton.addClickHandler(event -> close());
        saveButton.setStyleName("btn btn-primary");
        cancelButton.setStyleName("btn btn-sm btn-default");
    }

    private void setGoogleCookies(String serviceType) {
        Cookies.removeCookie(OFFICE_365_DATA_COKIE);
        Cookies.removeCookie(OFFICE_365_DRIVE_COKIE);
        Cookies.removeCookie(WEBSITE_URL_COOKIE);
        if (OFFICE_365_EVENTS.equals(serviceType)) {
            Cookies.setCookie(OFFICE_365_DATA_COKIE, serviceType);
        } else if (OFFICE_365_DOCUMENTS.equals(serviceType)) {
            Cookies.setCookie(OFFICE_365_DRIVE_COKIE, Utils.getLocationString().toString());
        }
    }

    public boolean validate() {
        int errorCount = 0;
        error.setHTML("");

        if (name.getText() == null || "".equals(name.getText().trim())) {
            errorCount++;
            error.setHTML("<b style='color:red;'>" + wfmStrings.fillRequiredField() + "</b>");
        } else if (!name.getText().startsWith("https://")) {
            errorCount++;
            error.setHTML("<b style='color:red;'>Please enter with https://</b>");
        }

        return errorCount <= 0;
    }
}