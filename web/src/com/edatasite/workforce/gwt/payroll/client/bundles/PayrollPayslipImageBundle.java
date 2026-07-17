package com.edatasite.workforce.gwt.payroll.client.bundles;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Nov 9, 2009
 * Time: 9:00:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PayrollPayslipImageBundle extends ClientBundle {

    @Source("com/edatasite/workforce/gwt/payroll/public/images/flow-charts/make-payment.jpg")
    ImageResource flow();

    @Source("com/edatasite/workforce/gwt/payroll/public/images/flow-charts/add-new-starter.jpg")
    ImageResource flowStarted();

}