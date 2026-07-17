package com.edatasite.workforce.gwt.accounting.client.ui.view.guide;

import com.edatasite.workforce.gwt.accounting.client.factory.AccountingSinksContainerFactory;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.ui.view.CompanySettings;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 07.05.2009
 * Time: 17:17:17
 * To change this template use File | Settings | File Templates.
 */
public class AccountingGettingStartedView extends CompanySettings implements Colapse {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    protected Widget onInitialize() {
        isAccountingGettingStarted = true;
        super.onInitialize();
        return null;
    }

    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        profileService.getCompanySettings(true, new AbstractAsyncCallback<SettingsData>() {
            public void success(SettingsData result) {
                LoadingPanel.loading(false);
                settingsData = result;
                setData();
            }
        });
    }

    protected void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true);
        AccountingService.App.get().completeAccountingGettingStarted(getDataForSave(), false, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                Info.show(accountingStrings.infoMessage34(), Info.Type.INFO);
                ((AccountingSinksContainerFactory) SinksContainerFactory.entryPoint.getContainerFactory()).finishGettingStarted();
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, (ClickHandler)clickEvent -> save());
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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