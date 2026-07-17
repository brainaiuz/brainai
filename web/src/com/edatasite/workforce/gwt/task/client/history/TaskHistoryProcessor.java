package com.edatasite.workforce.gwt.task.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.task.client.TaskAddSinksContainer;
import com.edatasite.workforce.gwt.task.client.TaskViewSinksContainer;

public class TaskHistoryProcessor implements HistoryProcessor {

    private WfmStrings wfmStrings = WfmStrings.App.get();


    public TaskHistoryProcessor() {

    }

    public SinksContainer process(String containerName, String[] strings) {
        return new TaskViewSinksContainer(containerName + strings[0], Property.get(Constants.TASK, wfmStrings.summaryView(), wfmStrings.task()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new TaskAddSinksContainer("taskadd", Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()), params);
    }

}
