package com.finnetlimited.reportservice.core.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * User: ${Dilsh0d}
 * Date: 16-Apr-2010
 * Time: 19:27:03
 */
public interface ExportBundle extends ClientBundle {

    ExportBundle instance = GWT.create(ExportBundle.class);

    @Source("com/finnetlimited/reportservice/core/client/bundle/pdf.png")
    ImageResource pdf();

    @Source("com/finnetlimited/reportservice/core/client/bundle/excel.png")
    ImageResource csv();
}
