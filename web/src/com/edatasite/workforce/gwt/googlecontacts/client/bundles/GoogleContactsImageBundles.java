package com.edatasite.workforce.gwt.googlecontacts.client.bundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Dec 1, 2009
 * Time: 2:00:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleContactsImageBundles extends ClientBundle {

    @ClientBundle.Source("com/edatasite/workforce/gwt/googlecontacts/client/bundles/icons/google-talk-small.gif")
    ImageResource googleContactTabIcon();

    class App {
        public static GoogleContactsImageBundles get() {
            return (GoogleContactsImageBundles) GWT.create(GoogleContactsImageBundles.class);
        }
    }
}
