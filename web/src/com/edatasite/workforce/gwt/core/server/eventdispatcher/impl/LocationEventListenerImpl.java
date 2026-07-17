package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.HolidayManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 22, 2010
 * Time: 6:28:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class LocationEventListenerImpl implements BusinessEventListener {

    public static String EMPLOYEE_LOCATION_CHANGE = "EMPLOYEE_LOCATION_CHANGE";

    public static WfmType<EdsLocation> TYPE = new WfmType<>(EventTypes.locationEventListener);
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private HolidayManager holidayManager;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;

    public void onAddEvent(EdsBusinessEvent event) {
        EdsLocation location = locationManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerLocationAddUpdate(location, creator, event.getTime());
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
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EMPLOYEE_LOCATION_CHANGE.equals(event.getEventType())) {
            Integer oldLocationId = event.getAdditionalSourceID();
            EdsLocation oldLocation = !oldLocationId.equals(0) ? locationManager.get(oldLocationId) : null;
            EdsEmployee employee = employeeManager.get(event.getSourceID());
            hrmsServiceLocal.updateHolidayDetails(employee, oldLocation, employee.getLocation());
        } else {
            String employeeIDs = event.getCustomStringField();
            if (employeeIDs != null && !"".equals(employeeIDs)) {
                List<EdsEmployee> employees = employeeManager.getEmployeesByIds(employeeIDs);
                if (employees != null && employees.size() > 0) {
                    employees.forEach(e -> {
                        Calendar todaysDate = new GregorianCalendar();
                        todaysDate.setTime(ServerUtils.getDayStartTime(new Date()));
                        EdsLocation location = locationManager.get(event.getEntityID());
                        List<EdsHoliday> newHolidays = holidayManager.getLocationHolidays(todaysDate.getTime(), location);
                        if (CollectionUtils.isNotEmpty(newHolidays)) {
                            newHolidays.forEach(h -> {
                                if (h.getStartDate().compareTo(e.getUserDate(new Date())) >= 0) {
                                    attendanceRawDataManager.updateHolidays(h, e.getObjectID(), h.isDayOff(), h.isTakeAnnual());
                                }
                            });
                        }
                    });
                }
            }
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

    public void onEditEvent(EdsBusinessEvent event) {
        EdsLocation location = locationManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerLocationEditUpdate(location, creator, event.getTime());
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

    public void onDeleteEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}