package com.finnetlimited.reportservice.core.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * User: ${Dilsh0d}
 * Date: 23-Mar-2010
 * Time: 19:57:36
 */
public interface MessageInfoBundle extends ClientBundle {

    MessageInfoBundle instance = GWT.create(MessageInfoBundle.class);

    @ClientBundle.Source("com/finnetlimited/reportservice/core/client/bundle/msgInfo.png")
    ImageResource msgInfo();

}
