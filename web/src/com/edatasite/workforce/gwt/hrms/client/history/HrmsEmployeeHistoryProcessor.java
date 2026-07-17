package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.EmployeeViewSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.HrmsEmployeeAddSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Nov 5, 2009
 * Time: 1:18:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class HrmsEmployeeHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    public SinksContainer process(String containerName, String[] strings) {
        return new EmployeeViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        //if (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
        return new HrmsEmployeeAddSinksContainer("hrmsemployeeadd", wfmStrings.addEmployee(), params);
        //}
        //return null;
    }
}
