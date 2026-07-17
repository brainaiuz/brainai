package com.edatasite.workforce.gwt.wfmTimer.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.wfmTimer.client.ui.ClockComponent;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 8/26/11
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */

public class WFMTimerViewSinksContainer extends SinksContainer {

    public WFMTimerViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.SHOW_TIMER) && Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_TIMER : PermissionConstants.CRM_TASKS_TIMER)) {
            addView(new ClockComponent(Integer.valueOf(params[0]), PM_TASK, params[1]));
        }
    }
}
