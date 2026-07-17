package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.assessment.client.ui.view.AppraisalsSettingsView;
import com.edatasite.workforce.gwt.assessment.client.ui.view.ValidityPeriodListView;
import com.edatasite.workforce.gwt.availability.client.NewAnnualLeaveAllowanceListView;
import com.edatasite.workforce.gwt.availability.client.ui.view.EmployeeBenefitAllowanceListView;
import com.edatasite.workforce.gwt.availability.client.ui.view.HolidayListView;
import com.edatasite.workforce.gwt.availability.client.ui.view.TimeslotListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.LogHistoryListView;
import com.edatasite.workforce.gwt.hrms.client.ui.PositionListView;
import com.edatasite.workforce.gwt.location.client.ui.LocationListView;
import com.edatasite.workforce.gwt.news.client.news.NewsCategoryListView;
import com.edatasite.workforce.gwt.profile.client.ui.AuditLogListView;
import com.edatasite.workforce.gwt.profile.client.ui.CertificateTypesListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.BenefitTypeListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.LeaveReasonListView;
import com.edatasite.workforce.gwt.team.client.ui.view.DepartmentListView;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: Mar 29, 2010
 * Time: 16:49:04 PM
 */
public class HrmsSettingsSinksContainer extends SinksContainer {

    public HrmsSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS)) {
            if (Utils.hasPermission(PermissionConstants.TIMESLOT_LIST)) {
                //Timeslot
                super.addView(new TimeslotListView());
            }

            if (Utils.hasPermission(PermissionConstants.HOLIDAY_LIST)) {
                //Public Holidays
                super.addView(new HolidayListView());
            }

            //Leave Reasons
            if (Utils.hasPermission(PermissionConstants.REFERENCE_LIST)) {
                super.addView(new LeaveReasonListView());
            }
            //annual leave allowance
            if (Utils.hasPermission(PermissionConstants.HRMS_ANNUAL_ALLOWANCE)) {
                super.addView(new NewAnnualLeaveAllowanceListView());
            }
            //Benefit Types
            if (Utils.hasPermission(PermissionConstants.BENEFIT_TYPE)) {
                super.addView(new BenefitTypeListView());
            }

            //Departments
            if (Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT)) {
                addView(new DepartmentListView(DepartmentListView.FROM_HRMS_SECTION));
            }

            //Positions
            if (Utils.hasPermission(PermissionConstants.HRMS_POSITION)) {
                addView(new PositionListView());
            }

            //Locations
            if (Utils.hasPermission(PermissionConstants.HRMS_LOCATION)) {
                addView(new LocationListView());
            }

            //Log History
            if (Utils.hasPermission(PermissionConstants.HRMS_VIEW_EMPLOYEE_CHANGE_LOG)) {
                addView(new LogHistoryListView(null));
            }

            if (Utils.hasPermission(PermissionConstants.SETTINGS_BENEFIT_ALLOWANCE)) {
                //Benefit Allowance
                addView(new EmployeeBenefitAllowanceListView());
            }

            //Certificate Templates
            if (Utils.hasPermission(PermissionConstants.CETIFICATE_TEMPLATE_LIST)) {
                super.addView(new CertificateTypesListView());
            }

            //New Categories
            if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_CATEGORIES)) {
                super.addView(new NewsCategoryListView());
            }

            if (Utils.hasPermission(PermissionConstants.SETTINGS_APPRAISAL_SETTINGS)) {
                //Appraisals Settings
                super.addView(new AppraisalsSettingsView());
            }

            if (Utils.hasPermission(PermissionConstants.SETTINGS_VALIDITY_PERIODS)) {
                //Validity Periods
                super.addView(new ValidityPeriodListView());
            }

            if (Utils.isSuperUser()) {
                super.addView(new AuditLogListView());
            }
        } else if (Utils.hasRole(ADMIN)) {
            //Departments
            addView(new DepartmentListView(DepartmentListView.FROM_HRMS_SECTION));

            //Positions
            addView(new PositionListView());

            //Locations
            addView(new LocationListView());
        }
    }
}
