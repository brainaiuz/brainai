package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.DisclosurePanelImages;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Aug 21, 2009
 * Time: 6:59:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MyDisclosurePanelImages extends DisclosurePanelImages {

    @Resource("com/edatasite/workforce/gwt/core/resource/minus.gif")
    AbstractImagePrototype disclosurePanelOpen();

    @Resource("com/edatasite/workforce/gwt/core/resource/plus.gif")
    AbstractImagePrototype disclosurePanelClosed();
}
