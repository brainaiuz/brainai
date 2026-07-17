package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.myaccount.client.bundles.MyaccountImageBundles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Fatxulla
 * Date: 11.03.16
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 */
public class NonPriceActiviraView extends View implements Constants {

    

    private FlexPanel mainContainer;

    public NonPriceActiviraView() {
        super("nonPrice", wfmStrings.currentSubscription());
    }

    @Override
    protected Widget onInitialize() {
        mainContainer = new FlexPanel();
        mainContainer.getElement().getStyle().setMarginTop(25, Style.Unit.PX);
        mainContainer.getElement().getStyle().setMarginLeft(50, Style.Unit.PX);
        mainContainer.setStyleName("mainTable");
        String a = "<a href='https://activira.com/pricing/' target='_blank'>https://activira.com/pricing/</a>";
        HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth("97%");
        panel.setHeight("97%");
        panel.add(new HTML("Please visit our pricing page :"+a+", in order to upgrade your subscription"));
        mainContainer.add(panel);

        add(mainContainer);
        return null;
    }


    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ImageResource getIconImage() {
        return MyaccountImageBundles.App.get().currentSubscription();
    }


    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
