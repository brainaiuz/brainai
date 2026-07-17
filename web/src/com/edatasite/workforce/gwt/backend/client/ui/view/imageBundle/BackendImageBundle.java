package com.edatasite.workforce.gwt.backend.client.ui.view.imageBundle;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 3, 2009
 * Time: 4:50:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BackendImageBundle extends ClientBundle {

    @ClientBundle.Source("com/edatasite/workforce/gwt/backend/public/images/on.png")
    ImageResource on();

    @ClientBundle.Source("com/edatasite/workforce/gwt/backend/public/images/off.png")
    ImageResource off();

}
