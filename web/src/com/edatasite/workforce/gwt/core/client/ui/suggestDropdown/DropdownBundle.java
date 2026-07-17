package com.edatasite.workforce.gwt.core.client.ui.suggestDropdown;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 05.11.2008
 * Time: 18:06:14
 * To change this template use File | Settings | File Templates.
 */
public interface DropdownBundle extends ClientBundle {

    @Source("com/edatasite/workforce/gwt/core/resource/dropdown/original.png")
    ImageResource original();

    @Source("com/edatasite/workforce/gwt/core/resource/dropdown/opened.png")
    ImageResource opened();

    @Source("com/edatasite/workforce/gwt/core/resource/dropdown/mouse_over.png")
    ImageResource mouseOver();

}
