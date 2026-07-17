package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.cashAdvanceReport.CashAdvanceReportView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.endOfServiceReport.EndOfServiceReportView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.pensionReport.PensionContributionReportView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.salaryReport.SalaryReportView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.wpsReport.WpsReportView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.05.14
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class PayrollReportSinksContainer extends SinksContainer {

    public PayrollReportSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.isArabicCompany()) {
            if (Utils.hasPermission(PermissionConstants.PAYROLL_WPS_REPORT)) {
                addView(new WpsReportView());
            }
            if (Utils.hasPermission(PermissionConstants.PAYROLL_END_OF_SERVICE_REPORT)) {
                addView(new EndOfServiceReportView());
            }
            if (Utils.hasPermission(PermissionConstants.PAYROLL_PENSION_CONTRIBUTION_REPORT)) {
                addView(new PensionContributionReportView());
            }
        }

        addView(new CashAdvanceReportView());
        addView(new SalaryReportView());
    }
}
