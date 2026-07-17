package com.finnetlimited.reportservice.core.client.bundle;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * User: ${Dilsh0d}
 * Date: 23-Mar-2010
 * Time: 16:27:45
 */
public interface ReportTypeBundle extends ClientBundle {

    ReportTypeBundle instance = GWT.create(ReportTypeBundle.class);

    @Source("com/finnetlimited/reportservice/core/client/bundle/tabular.jpg")
    ImageResource tabularImg();

    @Source("com/finnetlimited/reportservice/core/client/bundle/summaries.jpg")
    ImageResource summaryImg();
}
