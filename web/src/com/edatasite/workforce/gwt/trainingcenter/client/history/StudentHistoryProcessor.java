package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.StudentAddSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.StudentViewSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * User: Ilhombek
 * Date: 7/18/12
 * Time: 6:37 PM
 */
public class StudentHistoryProcessor implements HistoryProcessor {

    private static final TCStrings tcStrings = TCStrings.App.get();
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new StudentViewSinksContainer(containerName + strings[0], Property.get("students", tcStrings.summaryView(), tcStrings.students()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new StudentAddSinksContainer("studentsadd", Property.get("students", tcStrings.addMess(), tcStrings.students()), params);
    }
}