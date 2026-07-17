package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowAlertAddSinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowAlertViewSinksContainer;

/**
 * User: Hayot
 * Date: 28.02.2014
 * Time: 15:44:41
 */
public class WorkflowAlertHistoryProcessor implements HistoryProcessor {
    private SettingStrings settingsStrings = SettingStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();
    private WfmMessages wfmMessages = WfmMessages.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new WorkflowAlertViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        String desc = wfmStrings.addAlert();
        return new WorkflowAlertAddSinksContainer("workflowalertadd", desc, params);
    }
}
