package com.edatasite.workforce.gwt.myaccount.client.bundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 24.11.2009
 * Time: 15:18:00
 * To change this template use File | Settings | File Templates.
 */
public interface MyaccountImageBundles extends ClientBundle {

    @Source("com/edatasite/workforce/gwt/myaccount/client/bundles/icons/add-new-subscription-small.gif")
    ImageResource addNewSubscriptionSmall();

    @Source("com/edatasite/workforce/gwt/myaccount/client/bundles/icons/current-subscription.gif")
    ImageResource currentSubscription();

    @Source("com/edatasite/workforce/gwt/myaccount/client/bundles/icons/subscription-history-2.gif")
    ImageResource subscriptionHistory();

    @Source("com/edatasite/workforce/gwt/myaccount/resource/pricing/free.png")
    ImageResource free();

    @Source("com/edatasite/workforce/gwt/myaccount/resource/pricing/plus.png")
    ImageResource plus();

    @Source("com/edatasite/workforce/gwt/myaccount/resource/pricing/help.png")
    ImageResource help();

    @Source("com/edatasite/workforce/gwt/myaccount/resource/pricing/tick.png")
    ImageResource tick();

    @Source("com/edatasite/workforce/gwt/myaccount/resource/pricing/x-icon.png")
    ImageResource xicon();

    @Source("com/edatasite/workforce/gwt/myaccount/resource/pricing/currentplan.png")
    ImageResource currentplan();

    @Source("com/edatasite/workforce/gwt/myaccount/resource/pricing/bronze.png")
    ImageResource bronzeImage();

    @Source("com/edatasite/workforce/gwt/myaccount/resource/pricing/gold.png")
    ImageResource goldImage();

    @Source("com/edatasite/workforce/gwt/myaccount/resource/pricing/platinum.png")
    ImageResource platinumImage();

    class App {
        public static MyaccountImageBundles get() {
            return (MyaccountImageBundles) GWT.create(MyaccountImageBundles.class);
        }
    }

}
