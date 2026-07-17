package com.finnetlimited.reportservice.core.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * User: ${Dilsh0d}
 * Date: 24-Mar-2010
 * Time: 15:44:40
 */
public interface SummariesBundle extends ClientBundle {

    SummariesBundle instance = GWT.create(SummariesBundle.class);

    @Source("com/finnetlimited/reportservice/core/client/bundle/summariesCol.jpg")
    ImageResource summariesColInfo();
}
