package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.EducationAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.EducationViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: unni
 * Date: Dec 2, 2009
 * Time: 3:52:38 PM
 */
public class EducationHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new EducationViewSinksContainer(containerName + strings[0], hrmsStrings.educationView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new EducationAddSinksContainer("educationadd", hrmsStrings.addEducation(), params);
    }
}