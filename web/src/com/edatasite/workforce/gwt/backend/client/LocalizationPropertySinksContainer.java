package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.LocalizationListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.LocalizationPermissionView;
import com.edatasite.workforce.gwt.backend.client.ui.view.LocalizationSynchView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Fatkhulla Nigmatjonov
 * Date: 2/27/13
 * Time: 2:48 PM
 */
public class LocalizationPropertySinksContainer extends SinksContainer {

    boolean  isShowLocalizationPermission;
    public LocalizationPropertySinksContainer(String name, String description, boolean isShowLocalizationPermission) {
        super(name, description, isShowLocalizationPermission ? new String[]{"localizationPropertyView"} : null, NONE);

    }

    @Override
    protected void initViews() {
        addView(new LocalizationListView());
        setPreparedView("localizationPropertyView");
        if (params != null && "localizationPropertyView".equals(params[0])) {
            addView(new LocalizationPermissionView());
        }
        addView(new LocalizationSynchView());

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
