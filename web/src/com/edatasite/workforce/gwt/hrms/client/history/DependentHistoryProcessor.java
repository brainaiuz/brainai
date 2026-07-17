package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.DependentAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.DependentViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;


/**
 * User: unni
 * Date: Oct 21, 2009
 * Time: 10:02:11 PM
 */
public class DependentHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new DependentViewSinksContainer(containerName + strings[0], hrmsStrings.dependentView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new DependentAddSinksContainer("dependentadd", hrmsStrings.addDependant(), params);
    }
}