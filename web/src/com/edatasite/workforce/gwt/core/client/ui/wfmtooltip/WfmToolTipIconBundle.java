package com.edatasite.workforce.gwt.core.client.ui.wfmtooltip;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * User: Ilhombek
 * Date: 2/19/13
 * Time: 2:57 PM
 */
public interface WfmToolTipIconBundle extends ClientBundle {
    @Source("com/edatasite/workforce/gwt/core/client/bundles/icons/helptooltip.png")
    ImageResource helpIcon();

    @Source("com/edatasite/workforce/gwt/core/client/bundles/icons/warningtooltip.png")
    ImageResource warningIcon();
}