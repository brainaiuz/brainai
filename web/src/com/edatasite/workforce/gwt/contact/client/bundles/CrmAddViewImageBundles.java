package com.edatasite.workforce.gwt.contact.client.bundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Dec 20, 2009
 * Time: 5:38:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CrmAddViewImageBundles extends ClientBundle {

    @ClientBundle.Source("com/edatasite/workforce/gwt/core/client/bundles/icons/lead.jpg")
    ImageResource mailListAdvertisement();

    class App {
        public static CrmAddViewImageBundles get() {
            return (CrmAddViewImageBundles) GWT.create(CrmAddViewImageBundles.class);
        }
    }
}
