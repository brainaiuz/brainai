package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll;

import com.edatasite.workforce.core.domain.EdsMonthlyTimesheetPayment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.MonthlyOvertimeData;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.MonthlyTimesheetManager;
import com.edatasite.workforce.gwt.core.server.db.MonthlyTimesheetPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shohruh on 02-Oct-15.
 */
@Transactional
public class SinglePayrunEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsPayslipTableItem> TYPE = new WfmType<>(EventTypes.singlePayrunEventListener);
    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;
    @Autowired
    PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private MonthlyTimesheetManager monthlyTimesheetManager;
    @Autowired
    private MonthlyTimesheetPaymentManager monthlyTimesheetPaymentManager;
    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EdsMyUpdate.STATUS_CHANGE.equals(event.getEventType())) {
            String status = payslipTableItemManager.get(event.getEntityID()).getStatus() != null ? payslipTableItemManager.get(event.getEntityID()).getStatus().getName() : "";
            if (status.equalsIgnoreCase("Submitted")) {
                onSubmitEvent(event);
            } else if (status.equalsIgnoreCase("Approved")) {
                onApproveEvent(event);
            } else if (status.equalsIgnoreCase("Rejected")) {
                onRejectEvent(event);
            }
        }
    }

    private void onRejectEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSinglePayrunRejectUpdate(payslipTableItem, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private void onApproveEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSinglePayrunApproveUpdate(payslipTableItem, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private void onSubmitEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSinglePayrunSubmitUpdate(payslipTableItem, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSinglePayrunAddUpdate(payslipTableItem, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
        registerMonthlyTimesheetItemsPayments(payslipTableItem);
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSinglePayrunEditUpdate(payslipTableItem, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
        registerMonthlyTimesheetItemsPayments(payslipTableItem);
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSinglePayrunDeleteUpdate(payslipTableItem, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
        monthlyTimesheetPaymentManager.deleteItemsByPayslip(payslipTableItem.getObjectID());
    }

    private void registerMonthlyTimesheetItemsPayments(EdsPayslipTableItem payslipTableItem) {
        EdsEmployeePayrollSettings settings = employeePayrollSettingsManager.getEmployeeSettingValue(payslipTableItem.getEmployee().getObjectID(), Constants.RATE_TYPE);
        String rateType = settings != null && settings.getValue() != null && !settings.getValue().isEmpty() ? settings.getValue() : "";

        if (!Constants.FIXED_OVERTIME_RATE.equals(rateType)) {
            return;
        }

        //prev month monthly timesheet logic
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setPayrunID(payslipTableItem.getObjectID());
        fp.setEmployeeId(payslipTableItem.getEmployee().getObjectID());
        fp.setStartDate(payslipTableItem.getFromDate());

        List<MonthlyOvertimeData> prevMonthItems = monthlyTimesheetManager.getPrevMonthRemainingTimes(fp, false);
        List<MonthlyOvertimeData> currentMonthItems = monthlyTimesheetManager.getPrevMonthRemainingTimes(fp, true);
        monthlyTimesheetPaymentManager.deleteItemsByPayslip(payslipTableItem.getObjectID());

        List<MonthlyOvertimeData> paymentItems = new ArrayList<>();
        if (prevMonthItems != null && !prevMonthItems.isEmpty()) {
            paymentItems.addAll(prevMonthItems);
        }
        if (currentMonthItems != null && !currentMonthItems.isEmpty()) {
            paymentItems.addAll(currentMonthItems);
        }


        if (!paymentItems.isEmpty()) {
            for (MonthlyOvertimeData data : paymentItems) {
                EdsMonthlyTimesheetPayment payment = new EdsMonthlyTimesheetPayment();
                payment.setProjectEmployee(projectEmployeeManager.get(data.getProjectEmployeeID()));
                payment.setPayslip(payslipTableItem);
                payment.setMonthYear(data.getMonthYear());
                payment.setOvertime(data.getRegularOvertimeHours().doubleValue());
                payment.setWeekendOvertime(data.getWeeklyOvertimeHours().doubleValue());
                payment.setHolidayOvertime(data.getHolidayOvertimeHours().doubleValue());
                payment.setTotalDaysWorked(data.getTotalWorkedDays().doubleValue());
                monthlyTimesheetPaymentManager.create(payment);
            }
        }
    }
}
