package com.finnetlimited.reportservice.core.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * User: ${Dilsh0d}
 * Date: 24-Mar-2010
 * Time: 16:03:39
 */
public interface OrderReportBundle extends ClientBundle {
    OrderReportBundle instanse = GWT.create(OrderReportBundle.class);

     @Source("com/finnetlimited/reportservice/core/client/bundle/orderInfo.png")
     ImageResource orderInfo();
}
