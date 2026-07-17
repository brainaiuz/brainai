package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.team.client.services.client.impl.OrgBoardSettingsRestClient;
import com.edatasite.workforce.gwt.team.client.services.dto.OrgBoardSettingsItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class OrgBoardViewMenu extends PopupPanel {

    OrgBoardSettingsRestClient restClient = new OrgBoardSettingsRestClient();

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    interface Binder extends UiBinder<Widget, OrgBoardViewMenu> {
    }

    private static final Binder uiBinder = GWT.create(Binder.class);

    private OrgBoardSettingsItem settingsItem;

    @UiField
    KpiSwitcher showEmployees;
    @UiField
    KpiSwitcher showShortDescription;
    @UiField
    KpiSwitcher showDescription;
    @UiField
    KpiSwitcher showGoals;

    @UiField
    Label showEmployeeLabel;
    @UiField
    Label showDescriptionLabel;
    @UiField
    Label showShortDescriptionLabel;
    @UiField
    Label showGoalsLabel;

    @UiField
    LocationLookUpWithCode locationLookUp;


    public OrgBoardViewMenu() {
        super(true);
        setWidget(uiBinder.createAndBindUi(this));
        setStyleName("orgBoard-viewMenu");

        showEmployeeLabel.setText(hrmsStrings.showEmployees());
        showDescriptionLabel.setText(hrmsStrings.showWorkActiviteis());
        showShortDescriptionLabel.setText(hrmsStrings.showKeyDeliverable());
        showGoalsLabel.setText(hrmsStrings.showMetrics());

        init();
    }

    private void init() {
        LoadingPanel.loading(true);
        restClient.getOrgBoardSettings(new AsyncCallback<ResultTO<OrgBoardSettingsItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<OrgBoardSettingsItem> result) {
                LoadingPanel.loading(false);
                settingsItem = result.getData();
                initData();
                initHandler();
            }
        });
    }

    private void initData() {
        showEmployees.setValue(settingsItem.getShowEmployees());
        showShortDescription.setValue(settingsItem.getShowShortDescription());
        showDescription.setValue(settingsItem.getShowDescription());
        showGoals.setValue(settingsItem.getShowGoals());
        SelectItem defaultView = new SelectItem(null, hrmsStrings.seeAll());
        locationLookUp.addItem(defaultView);
        if (settingsItem.getLocation() != null) {
            locationLookUp.setSelected(settingsItem.getLocation());
        }else {
            locationLookUp.getSuggestBox().setText(hrmsStrings.seeAll());
        }
        new KpiToolTip(locationLookUp, hrmsStrings.locationLookupTooltip());

    }

    private void initHandler() {
        showEmployees.addValueChangeHandler(h -> {
            settingsItem.setShowEmployees(showEmployees.getValue());
            updateSettings(settingsItem);
        });
        showShortDescription.addValueChangeHandler(h -> {
            settingsItem.setShowShortDescription(showShortDescription.getValue());
            updateSettings(settingsItem);
        });
        showDescription.addValueChangeHandler(h -> {
            settingsItem.setShowDescription(showDescription.getValue());
            updateSettings(settingsItem);
        });
        showGoals.addValueChangeHandler(h -> {
            settingsItem.setShowGoals(showGoals.getValue());
            updateSettings(settingsItem);
        });
        locationLookUp.getSuggestBox().addSelectionHandler(h -> {
            settingsItem.setLocationId(locationLookUp.getSelectedItemID());
            settingsItem.setLocation(locationLookUp.getSelectedItem());
            updateSettings(settingsItem);
        });

    }

    private void updateSettings(OrgBoardSettingsItem item) {
        LoadingPanel.loading(true);
        restClient.updateOrgBoardSettings(item, new AsyncCallback<ResultTO<OrgBoardSettingsItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<OrgBoardSettingsItem> result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ORG_BOARD_SETTINGS_UPDATED, result, OrgBoardViewMenu.this);
            }
        });
    }

    public boolean isShowEmployees() {
        return showEmployees.getValue();
    }

    public boolean isShowResult() {
        return showShortDescription.getValue();
    }

    public boolean isShowWorks() {
        return showDescription.getValue();
    }

    public boolean isShowMetrics() {
        return showGoals.getValue();
    }
}