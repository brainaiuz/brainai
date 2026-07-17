package com.edatasite.workforce.gwt.payroll.client.bundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Nov 9, 2009
 * Time: 8:44:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PayrollStarterImageBundle extends ClientBundle {

    @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/public/images/flow-charts/add-new-starter.jpg")
    ImageResource flow();

    class App {
        public static PayrollStarterImageBundle get() {
            return (PayrollStarterImageBundle) GWT.create(PayrollStarterImageBundle.class);
        }
    }


}
