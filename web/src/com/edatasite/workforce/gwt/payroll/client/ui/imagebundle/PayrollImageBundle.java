package com.edatasite.workforce.gwt.payroll.client.ui.imagebundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jan 26, 2010
 * Time: 8:14:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PayrollImageBundle extends ClientBundle {
    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/approved.png")
    ImageResource approved();

    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/canceled.png")
    ImageResource canceled();

    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/reccuring-payment_16.png")
    ImageResource reccur_payment_16();

    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/bank_16.png")
    ImageResource bank_16();

    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/aeo_16.png")
    ImageResource aeo_16();

    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/efile-2.png")
    ImageResource efile_2();

    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/payroll-settings.png")
    ImageResource payroll_settings();

    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/payment-deduction.png")
    ImageResource payment_deduction();

    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/payslip.png")
    ImageResource payslip();

    class App {
        public static PayrollImageBundle get() {
            return (PayrollImageBundle) GWT.create(PayrollImageBundle.class);
        }
    }


}
