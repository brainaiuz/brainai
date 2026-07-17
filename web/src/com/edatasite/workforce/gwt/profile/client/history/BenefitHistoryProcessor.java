package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.BenefitSinksContainer;
import com.edatasite.workforce.gwt.profile.client.BenefitViewSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * Created by Aziz on 08.09.14.
 */
public class BenefitHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new BenefitViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        String description = settingsStrings.addBenefit();
        if (params.length == 2 && params[1] != null) {
            description = settingsStrings.editBenefit();
        }
        if (Utils.hasPermission(PermissionConstants.BENEFIT_TYPE_ADD)) {
            return new BenefitSinksContainer("benefitadd", description, params);
        }
        return null;
    }
}
