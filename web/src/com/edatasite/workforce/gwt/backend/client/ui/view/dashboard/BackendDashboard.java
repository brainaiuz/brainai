package com.edatasite.workforce.gwt.backend.client.ui.view.dashboard;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by Abdulaziz on 21.04.2016.
 */
public class BackendDashboard extends Composite implements Constants {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    interface ManagerDashboardUiBinder extends UiBinder<HTMLPanel, BackendDashboard> {
    }

    private static ManagerDashboardUiBinder ourUiBinder = GWT.create(ManagerDashboardUiBinder.class);
    private String manageType;//by user or by company

    @UiField
    HTMLPanel leftContent;
    @UiField
    HTMLPanel leftOneContent;
ExpiringCompaniesWidget expiringCompaniesWidget;
  CompaniesNotLoggedWidget companiesNotLoggedWidget;

    public BackendDashboard() {
        HTMLPanel rootPanel = ourUiBinder.createAndBindUi(this);
        initWidget(rootPanel);
        onInitialize();
    }

    private void onInitialize() {
        expiringCompaniesWidget = new ExpiringCompaniesWidget();
         leftContent.add(expiringCompaniesWidget);

        companiesNotLoggedWidget = new CompaniesNotLoggedWidget();
        leftOneContent.add(companiesNotLoggedWidget);

    }
}
