package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.SubscriptionManagementItem;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabBar;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionLabel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 4/18/12
 * Time: 4:14 PM
 */
public class SubscriptionManagementSummaryView extends FooteredView implements Colapse {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final Integer companyID;
    private PreviewSectionContainer sectionContainer;

    public SubscriptionManagementSummaryView(Integer companyID) {
        super("subscriptionManagementSummary", backendStrings.subscriptionSummary());
        this.companyID = companyID;
    }

    @Override
    public String getIconStyle() {
        return "icon-subscriptionManagementSummary";
    }

    @Override
    protected Widget onInitialize() {
        drawInitialize();
        return null;
    }

    private void drawInitialize() {
        sectionContainer = new PreviewSectionContainer();
        LoadingPanel.loading(true);
        BackendService.App.get().getCompanySubscriptionManagementItem(companyID, new AbstractAsyncCallback<SubscriptionManagementItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SubscriptionManagementItem result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    draw(result);
                }
                add(sectionContainer);
            }
        });
    }

    private void draw(SubscriptionManagementItem item) {
        PreviewSectionField sectionField;
        PreviewSectionLabel sectionLabel;
        sectionLabel = new PreviewSectionLabel(backendStrings.companySummary(), "");
        sectionField = new PreviewSectionField(null, null);
        sectionField.addStyleName("previewSection-short-summary");

        sectionField.addField(backendStrings.companyID(), item.getCompanyId().toString());
        sectionField.addField(wfmStrings.companyName(), item.getCompanyName());
        sectionField.addField(backendStrings.adminEmail(), item.getAdminEmail());
        sectionField.addField(wfmStrings.registeredDate(), item.getRegistrationDate());
        sectionContainer.addSection(sectionLabel, sectionField);

        sectionLabel = new PreviewSectionLabel(backendStrings.usagePlanHistory(), "");
        sectionField = new PreviewSectionField(null, null);
        SubscriptionAsPaidTab asPaidTab = new SubscriptionAsPaidTab(backendStrings.usagePlanHistory(), item.getCompanyId());
        asPaidTab.setScroll(true);
        CustomTabBar customTabBar = new CustomTabBar(1);
        customTabBar.setPanelSize(1000, 200);
        customTabBar.setStyleName("subscriptionManagementSummary");
        customTabBar.addWidget(asPaidTab);
        customTabBar.selectTab(0);
        sectionField.addWidget(customTabBar);
        sectionContainer.addSection(sectionLabel, sectionField);
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