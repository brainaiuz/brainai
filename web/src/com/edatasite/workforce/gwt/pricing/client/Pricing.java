package com.edatasite.workforce.gwt.pricing.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Created by IntelliJ IDEA.
 * User: muratov
 * Date: Jan 31, 2010
 * Time: 6:31:20 PM
 */
public class Pricing implements EntryPoint {

    public interface PricingResource extends ClientBundle {
        @CssResource.NotStrict
        @Source("com/edatasite/workforce/gwt/pricing/client/Pricing.css")
        CssResource pricingCss();
    }

    public static final PricingResource pricingResource = GWT.create(PricingResource.class);

    public void onModuleLoad() {
        pricingResource.pricingCss().ensureInjected();
//        RootPanel.get("container").add(new PricingSubscribeView());
        RootPanel.get("container").add(new PricingSubscribeView2());
    }
}
