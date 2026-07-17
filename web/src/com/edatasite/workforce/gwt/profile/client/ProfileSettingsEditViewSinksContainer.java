package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.SettingsEmployeeEditForm;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 8/2/11
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfileSettingsEditViewSinksContainer extends SinksContainer {

    public ProfileSettingsEditViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new SettingsEmployeeEditForm(id));
    }
}
