package com.edatasite.workforce.gwt.assessment.client.ui.history;

import com.edatasite.workforce.gwt.assessment.client.TemplateSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class TemplateHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new TemplateSinksContainer(containerName + strings[0], wfmStrings.template(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new TemplateSinksContainer("addTemplate", wfmStrings.manageTemplates());
    }
}