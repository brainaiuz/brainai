package com.edatasite.workforce.gwt.project.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ProjectAddSinksContainer;
import com.edatasite.workforce.gwt.project.client.ProjectViewSinksContainer;

public class ProjectHistoryProcessor implements HistoryProcessor {

    private WfmStrings wfmStrings = WfmStrings.App.get();

    public ProjectHistoryProcessor() {
    }

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ProjectViewSinksContainer(containerName + strings[0], Property.get(Constants.PROJECT, wfmStrings.summaryView(), wfmStrings.project()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ProjectAddSinksContainer("projectadd", Property.get(Constants.PROJECT, wfmStrings.project()), params);
    }
}