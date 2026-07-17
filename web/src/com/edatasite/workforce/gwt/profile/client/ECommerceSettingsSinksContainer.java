package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.ShopifyIntegrationView;
import com.edatasite.workforce.gwt.profile.client.ui.view.SynchronizeWithMagentoView;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: 2019-06-20 14:24
 */
public class ECommerceSettingsSinksContainer extends SinksContainer {

    public ECommerceSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.MAGENTO_INTEGRATION_ENABLE)) {
            addView(new SynchronizeWithMagentoView());
            addView(new ShopifyIntegrationView());
        }
    }
}
