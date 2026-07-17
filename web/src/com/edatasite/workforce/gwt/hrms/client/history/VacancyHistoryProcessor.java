package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.VacancyAddEditSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.VacancySinksContainer;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/22/12
 * Time: 5:26 PM
 */
public class VacancyHistoryProcessor implements HistoryProcessor {

    private HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new VacancySinksContainer(containerName + strings[0], hrmsStrings.vacancyView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new VacancyAddEditSinksContainer("vacancyadd", wfmStrings.add() + " " + Property.get("vacancy", wfmStrings.vacancy()), params);
    }
}