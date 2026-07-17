package com.edatasite.workforce.gwt.task.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.task.client.MultiTaskAddSinksContainer;

public class MultiTaskHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return /*new TaskViewSinksContainer(containerName+strings[0], "Task View", strings)*/null;
    }

    public SinksContainer processAdd(String[] params) {
        return new MultiTaskAddSinksContainer("multitaskadd", Property.get(Constants.TASK, wfmStrings.addMultiTask(), wfmStrings.task()), params);
    }

}