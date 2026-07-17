package com.edatasite.workforce.gwt.project.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ProjectBudgetSheetViewSinksContainer;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 8/15/11
 * Time: 5:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBudgetSheetHistoryProcessor implements HistoryProcessor {
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ProjectBudgetSheetViewSinksContainer(containerName + strings[0], projectStrings.employeeCostDetails(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
