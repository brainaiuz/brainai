package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.GoogleAuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 12, 2011
 * Time: 12:30:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleAnalyticsSettingsView extends View implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileServiceAsync profileService = ProfileService.App.get();

    private WfmForm table;
    private SimpleLink delGoogleAnalyticsToken;
    private SimpleLink getGoogleAnalyticsToken;

    public GoogleAnalyticsSettingsView() {
        super("googleanalytics", wfmStrings.googleAnalytics());
    }



    protected Widget onInitialize() {
        initInternal();
        return null;
    }

    private void initInternal() {
        table = new WfmForm(new String[]{"30%", "70%"});
        table.setCellSpacing(10);
        table.addField(null, new Label());
        table.setLabelSize("250px");
        table.setLabelAlignment(WfmForm.ALIGN_LEFT);
        table.addHorizontalLine();
        table.addTitleField(settingsStrings.authorizedAccess() + "&nbsp;" + Utils.getProductName() + "&nbsp;" + wfmStrings.accountCrm());

        delGoogleAnalyticsToken = new SimpleLink("[" + settingsStrings.revokeAccess() + "]");
        delGoogleAnalyticsToken.addClickHandler(clickEvent -> {
            clear();
            initInternal();
        });
        getGoogleAnalyticsToken = new SimpleLink(settingsStrings.configureWithGoogleAnalyticsAccount());
        getGoogleAnalyticsToken.addClickHandler(clickEvent -> GoogleAuthorizationPanel.redirectToGoogleAnalyticsAuthPage());

        ProfileService.App.get().validateGoogleAnalytics(new AbstractAsyncCallback<Boolean>(){
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(Boolean aBoolean) {
                if(aBoolean) {
                    table.addField(wfmStrings.googleAnalytics(), delGoogleAnalyticsToken);
                } else {
                    table.addField(wfmStrings.googleAnalytics(), getGoogleAnalyticsToken);
                }
                add(table);
            }
        });
    }

    public String getIconStyle() {
        return "icon-settings-user-credentials";
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