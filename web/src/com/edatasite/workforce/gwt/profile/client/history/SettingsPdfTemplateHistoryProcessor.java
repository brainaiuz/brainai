package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.SettingsPdfTemplateSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * User: Abror Abdukadirov
 * Date: 07.12.2018 18:24
 */
public class SettingsPdfTemplateHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SettingsPdfTemplateSinksContainer(containerName + strings[0], settingsStrings.pdfSettings(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
