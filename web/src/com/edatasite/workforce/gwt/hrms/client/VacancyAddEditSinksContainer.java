package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.AddVacancyView;

import java.util.LinkedList;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/22/12
 * Time: 5:32 PM
 */

public class VacancyAddEditSinksContainer extends SinksContainer {

    public VacancyAddEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params != null && COPY.equals(params[2])) {
            addView(new AddVacancyView(Integer.valueOf(params[1]), true));
        } else if (params != null && "positionId".equals(params[1]) && (Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY) || Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY_FOR_CURRENT_POSITION))) {
            addView(new AddVacancyView(true, Integer.valueOf(params[2])));
        } else if (Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY)) {
            addView(new AddVacancyView(null));
        }
    }
}