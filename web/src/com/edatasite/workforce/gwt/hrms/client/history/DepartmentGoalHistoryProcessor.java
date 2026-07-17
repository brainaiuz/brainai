package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GoalAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 23, 2009
 * Time: 4:54:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DepartmentGoalHistoryProcessor implements HistoryProcessor {
    private HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new GoalAddSinksContainer("departmentgoaladd", Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.addDepartmentGoal(), wfmStrings.department()), params);
    }

}