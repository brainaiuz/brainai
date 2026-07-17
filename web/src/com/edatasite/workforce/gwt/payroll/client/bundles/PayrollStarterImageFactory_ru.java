package com.edatasite.workforce.gwt.payroll.client.bundles;

import com.google.gwt.core.client.GWT;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 31.10.11
 * Time: 18:48
 * To change this template use File | Settings | File Templates.
 */
public class PayrollStarterImageFactory_ru implements PayrollStarterImageFactory{

    @Override
    public PayrollStarterImageBundle createImageBundle() {
        return (PayrollStarterImageBundle) GWT.create(PayrollStarterImageBundle_ru.class);
    }
}
