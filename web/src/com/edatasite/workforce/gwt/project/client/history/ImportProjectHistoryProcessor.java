package com.edatasite.workforce.gwt.project.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ImportProjectSinksContainer;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;

/**
 * Created by Normurod on 9/19/15.
 */
public class ImportProjectHistoryProcessor implements HistoryProcessor {
    private ProjectStrings projectStrings = ProjectStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportProjectSinksContainer("importprojectadd", projectStrings.importingProject(), params);
    }
}
