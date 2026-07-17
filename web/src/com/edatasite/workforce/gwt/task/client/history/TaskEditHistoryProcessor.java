package com.edatasite.workforce.gwt.task.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.task.client.TaskEditSinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: Azazello
 * Date: 12/22/14
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskEditHistoryProcessor implements HistoryProcessor {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new TaskEditSinksContainer(containerName + strings[0], wfmStrings.edit(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
