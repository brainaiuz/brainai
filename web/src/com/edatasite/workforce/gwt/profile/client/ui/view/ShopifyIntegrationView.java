package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Abror Abdukadirov
 * Date: 16.05.2019 17:34
 */
public class ShopifyIntegrationView extends CustomForm implements CustomFormConstants, Colapse {

    public ShopifyIntegrationView() {
        super("shopify", "Shopify");
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    protected void addButtons() {

    }

    @Override
    protected void getDataToFillFields() {

    }

    private void initialize() {
        HTMLPanel comingSoonPanel = new HTMLPanel("");
        HTML comingSoonText = new HTML("Coming Soon");
        comingSoonText.setStyleName("comming_soon");
        comingSoonPanel.add(comingSoonText);
        addField("SHOPIFY", comingSoonPanel);
        this.show();
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.SHOPIFY_INTEGRATION_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
