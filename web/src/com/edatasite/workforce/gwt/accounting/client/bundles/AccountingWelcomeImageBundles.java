package com.edatasite.workforce.gwt.accounting.client.bundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 26.11.2009
 * Time: 16:36:21
 * To change this template use File | Settings | File Templates.
 */
public interface AccountingWelcomeImageBundles extends ClientBundle {

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/client/bundles/icons/pick-list.png")
    ImageResource pickList();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/client/bundles/icons/warehouse.png")
    ImageResource warehouse();

    class App {
        public static AccountingWelcomeImageBundles get() {
            return (AccountingWelcomeImageBundles) GWT.create(AccountingWelcomeImageBundles.class);
        }
    }


}
