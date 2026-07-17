package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.core.client.Utils;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PseudoMenuItem;
import com.edatasite.workforce.gwt.core.client.rpc.fakeContainer.PseudoContainerServiceAsync;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class SettingsLink extends ModuleLink {
    
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SettingsLink() {
        super("main-modules__item--settings", SvgEnum.settings, "Settings");
    }
    public void activate() {
        if (Constants.MODULE_SETTINGS.equals(GWT.getModuleName())) {
//            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SIDE_NAV_POSITION_CHANGE, null, SettingsLink.this);
            MainLayout.get().resetSideNavContent();
        } else {
            List<PseudoMenuItem> settingsMenuItems = Utils.getPseudoContainer().getSettingsPseudoMenuItems();
            if (settingsMenuItems == null || settingsMenuItems.size() == 0) {
                loadSettingsMenuItems();
            } else {
                showSettingsPseudoMenuitems();
            }
        }
    }

    private void loadSettingsMenuItems() {
        MainLayout.get().loadSideNavContent(true);
        PseudoContainerServiceAsync.App.get().getSettingsMenuItems(new AsyncCallback<ArrayList<PseudoMenuItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.warn(wfmStrings.error());
                MainLayout.get().loadSideNavContent(false);
            }

            @Override
            public void onSuccess(ArrayList<PseudoMenuItem> result) {
                MainLayout.get().loadSideNavContent(false);
                Utils.getPseudoContainer().setSettingsPseudoMenuItems(result);
                showSettingsPseudoMenuitems();
            }
        });
    }

    private void showSettingsPseudoMenuitems() {
        MainLayout.get().modulesBar.generatePseudoSideNavMenu(Constants.SETTINGS_URL, wfmStrings.settings(), Utils.getPseudoContainer().getSettingsPseudoMenuItems());
//        MainLayout.get().setSideMenuOpen(true);
    }
}
