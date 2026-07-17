package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.ui.view.newProjectBudgetSheet.ProjectBudgetSheet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class NewProjectBudgetSheetView extends View implements FittedContent {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private Integer projectId;
    private boolean hasAccessToChange = true;

    public NewProjectBudgetSheetView(Integer projectId, boolean hasAccessToChange) {
        super("projectBudgetSpecific", projectStrings.budgetSheet());
        this.projectId = projectId;
        this.hasAccessToChange = hasAccessToChange;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        AccountingService.App.get().getCompanyFinancialSettings(new AsyncCallback<FinancialSettingsItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn("Error! Could not load company financial settings.");
            }

            @Override
            public void onSuccess(FinancialSettingsItem financialSettingsItem) {
                add(new ProjectBudgetSheet(projectId, hasAccessToChange));
            }
        });
        return null;
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
    }

    @Override
    public String getIconStyle() {
        return "";
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        MainLayout.get().considerBodyHasOperPanel(true);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        MainLayout.get().considerBodyHasOperPanel(false);
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
