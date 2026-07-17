package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.AssessmentViewSinkContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Abdullo
 * Date: 20.09.12
 * Time: 17:52
 */
public class AssessmentViewHistoryProcessor implements HistoryProcessor {

    private static TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AssessmentViewSinkContainer(containerName + strings[0], tcStrings.assessmentView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
