package com.edatasite.workforce.gwt.modulesettings.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 06.05.14
 * Time: 19:44
 * To change this template use File | Settings | File Templates.
 */
public class ModuleSettingsSinksContainer extends SinksContainer {

    public ModuleSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, params == null ? NONE : CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String companyId = null;
        if (params != null && params.length > 0) {
            companyId = params[0];
        }
        if (companyId != null && !"".equals(companyId)) {
            addView(new PriceTable(Integer.valueOf(companyId)));
        } else {
            addView(new PriceTable());
        }
    }
}
