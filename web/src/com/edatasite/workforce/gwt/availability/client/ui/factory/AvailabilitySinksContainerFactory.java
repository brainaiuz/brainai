package com.edatasite.workforce.gwt.availability.client.ui.factory;

import com.edatasite.workforce.gwt.availability.client.AvailabilitySinksContainer;
import com.edatasite.workforce.gwt.availability.client.history.AvailabilityHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.HolidayHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.LeaveRequestViewHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.TimeslotHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.employee.client.history.EmployeeHistoryProcessor;

public class AvailabilitySinksContainerFactory extends SinksContainerFactory {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public AvailabilitySinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("availability");
    }

    public void initDefaultContainers() {
        if (!Utils.hasRole(CLIENT)) {
            SinksContainer availability = new AvailabilitySinksContainer("availability", wfmStrings.attendanceTracking());
            showPrepairedView(availability, AVAILA_LANDING_VIEW, AVAILA_HOME_VIEW, AVAILABILITY_FIRST_VIEW);
        }
    }


    public void registerProcessors() {
        registerHistoryProcessor("availability", new AvailabilityHistoryProcessor());
        registerHistoryProcessor("timeslot", new TimeslotHistoryProcessor());
        registerHistoryProcessor("holiday", new HolidayHistoryProcessor());
        registerHistoryProcessor("employee", new EmployeeHistoryProcessor());
        registerHistoryProcessor("leaverequest", new LeaveRequestViewHistoryProcessor());
    }

    public void registerMenuItems() {

        addNewMenuItem(wfmStrings.leaveRequest(), "availability|add/add");
        if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(TL)) {
            addNewMenuItem(wfmStrings.timeslot(), "timeslot|add/add");
        }
        if (Utils.hasRole(DR) || Utils.hasRole(ADMIN)) {
            addNewMenuItem(wfmStrings.holiday(), "holiday|add/add");
        }
        addNewMenuItem(wfmStrings.incident(), "incident|add/add");
    }
}
