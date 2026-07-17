package com.edatasite.workforce.gwt.payroll.client.bundles;

import com.google.gwt.core.client.GWT;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 31.10.11
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public class PayrollPayslipImageFactory_ru implements PayrollPayslipImageFactory {
    @Override
    public PayrollPayslipImageBundle createImageBundle() {
        return (PayrollPayslipImageBundle) GWT.create(PayrollPayslipImageBundle_ru.class);
    }
}
