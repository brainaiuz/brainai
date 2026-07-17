package com.edatasite.workforce.gwt.payroll.client.bundles;

import com.google.gwt.core.client.GWT;

/**
 * Created by IntelliJ IDEA.
 * User: WFT01
 * Date: 31.10.11
 * Time: 18:35
 * To change this template use File | Settings | File Templates.
 */
public class PayrollStarterImageFactory_ implements PayrollStarterImageFactory{


    @Override
    public PayrollStarterImageBundle createImageBundle() {
        return (PayrollStarterImageBundle) GWT.create(PayrollStarterImageBundle.class);
    }
}
