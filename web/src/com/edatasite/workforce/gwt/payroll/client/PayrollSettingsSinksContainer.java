package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.EmployerSettingsSummaryView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.EndOfServiceSettingsView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollCategoryV2ListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollGlobalSettingsListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollGroupsListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollNumberingSettingsForm;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollZoneListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PensionProviderListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PensionSchemaSummaryView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.dailyRateCalculation.DailyRateCalculationView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.sickLeaveSettings.SickLeaveSettingsVIew;
import com.edatasite.workforce.gwt.profile.client.ui.view.MinimumWageListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.WageRateListView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 28.04.14
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class PayrollSettingsSinksContainer extends SinksContainer {

    public PayrollSettingsSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_EMPLOYER_SETTINGS)) {
            addView(new EmployerSettingsSummaryView());
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_LIST)) {
            addView(new PayrollGroupsListView());
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYMENT_DEDUCATION_LIST)) {
            addView(new PayrollGlobalSettingsListView());
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_PENSION_PROVIDERS)) {
            addView(new PensionProviderListView());
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_PENSION_SCHEMES)) {
            addView(new PensionSchemaSummaryView());
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_CATEGORIES_LIST)) {
            addView(new PayrollCategoryV2ListView());
        }
        /*if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_DEDUCATION_CATEGORIES)) {
            PaymentDeductionCategoryListView dlv = new PaymentDeductionCategoryListView("1");
            addView(dlv);
        }
        PaymentDeductionCategoryListView emp = new PaymentDeductionCategoryListView("2");
        addView(emp);*/

        if (Utils.isArabicCompany()) {
            addView(new EndOfServiceSettingsView());
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.SICK_LEAVE_SETTINGS_CALCULATION)) {
            addView(new SickLeaveSettingsVIew());
            addView(new DailyRateCalculationView());
        }
        addView(new PayrollNumberingSettingsForm());
        addView(new PayrollZoneListView());
        addView(new MinimumWageListView());
        addView(new WageRateListView());
    }
}
