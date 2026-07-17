package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowEventAddSinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowEventViewSinksContainer;

/**
 * User: Hayot
 * Date: 28.02.2014
 * Time: 15:44:41
 */
public class WorkflowEventHistoryProcessor implements HistoryProcessor {
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final WfmMessages wfmMessages = WfmMessages.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new WorkflowEventViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        String desc = wfmMessages.add(params != null && params.length > 3 && params[3].equals("call") ? Property.get(Constants.LOGACALL, wfmStrings.logCall()) : Property.get(Constants.EVENT_LIST, wfmStrings.event()));
        return new WorkflowEventAddSinksContainer("workfloweventadd", desc, params);
    }
}
