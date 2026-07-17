package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/26/15
 * Time: 5:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SinglePayrunInterface {

    Integer getSinglePayrunID();

    Boolean isDoubleApprovedEnabled();

    Boolean isCalculatePension();

    Boolean isAtsCustomizationEnabled();

    SinglePayrunGenerateView getView();

    PayslipItemFilter getFilter();
}
