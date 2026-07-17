package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.InstructorAddSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.InstructorViewSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * User: Ilhombek
 * Date: 8/6/12
 * Time: 5:32 PM
 */
public class InstructorHistoryProcessor implements HistoryProcessor {

    private static final TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new InstructorViewSinksContainer(containerName + strings[0], tcStrings.instructorView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new InstructorAddSinksContainer("tcInstructoradd", tcStrings.addInstructor(), params);
    }
}