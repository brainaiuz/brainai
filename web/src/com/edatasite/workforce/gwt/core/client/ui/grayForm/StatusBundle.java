package com.edatasite.workforce.gwt.core.client.ui.grayForm;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.12.2008
 * Time: 16:25:34
 * To change this template use File | Settings | File Templates.
 */
public interface StatusBundle extends ClientBundle {

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/resource/statusIcons/info.gif")
    ImageResource info();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/resource/statusIcons/success.gif")
    ImageResource success();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/resource/statusIcons/warning.gif")
    ImageResource warning();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/resource/statusIcons/error.gif")
    ImageResource error();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/resource/statusIcons/validation.gif")
    ImageResource validation();
}
