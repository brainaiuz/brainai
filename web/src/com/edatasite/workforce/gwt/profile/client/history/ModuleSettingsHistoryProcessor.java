package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.modulesettings.client.ModuleSettingsSinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 06.05.14
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
public class ModuleSettingsHistoryProcessor implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ModuleSettingsSinksContainer(containerName + strings[0], WfmStrings.App.get().moduleSettings(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
