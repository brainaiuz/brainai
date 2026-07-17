package com.edatasite.workforce.gwt.team.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.team.client.DepartmentAddSinksContainer;
import com.edatasite.workforce.gwt.team.client.ui.DepartmentViewSinksContainer;

public class TeamHistoryProcessor implements HistoryProcessor {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new DepartmentViewSinksContainer(containerName + strings[0], Property.get(Constants.DEPARTMENT_LIST, settingsStrings.departmentView(), wfmStrings.department()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new DepartmentAddSinksContainer("departmentadd", Property.get(Constants.DEPARTMENT_LIST, wfmStrings.addMess(), wfmStrings.department()));

    }
}
