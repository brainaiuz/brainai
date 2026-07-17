package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Omonullo on 1/4/2017.
 */
public class SignupCompanySettingsView extends View implements Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    public SignupCompanySettingsView() {
        super("signUpCompanySettings", wfmStrings.company());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        SignupCompanySettingsViewUIBinder uiBinder = new SignupCompanySettingsViewUIBinder(new SignupCompanySettingsViewInterface() {

        });
        add(uiBinder.getRootElement());
        getElement().getStyle().setProperty("height", "auto");
        LoadingPanel.loading(false);
        return null;
    }

    @Override
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
