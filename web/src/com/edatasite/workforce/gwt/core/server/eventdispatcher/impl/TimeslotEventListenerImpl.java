package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsDate;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.TransactionHelper;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.DateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;

/**
 * User: Eminem
 * Date: 16.05.12
 * Time: 14:15
 */
@Transactional
public class TimeslotEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsEmployee> TYPE = new WfmType<>(EventTypes.timeslotEventListener);
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private DateManager dateManager;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private TransactionHelper transactionHelper;

    private final Logger logger = LoggerFactory.getLogger(TimeslotEventListenerImpl.class);

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        logger.info("UPDATE THE TIME SLOT FOR COMPANY -> {} HAS BEGUN", SecurityContext.getCompanyID());
        EdsTimeSlot timeSlot = timeSlotManager.get(event.getSourceID());
        if (timeSlot == null) {
            return;
        }

        final int BATCH = 50;
        List<Integer> ids = Arrays.stream(event.getCustomStringField().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::valueOf)
                .distinct()
                .toList();

        EdsTimeSlotItem[] timeslotMinutes = availabilityServiceLocal.getTimeslotMinutesArray(timeSlot);

        for (int i = 0; i < ids.size(); i += BATCH) {
            List<Integer> sub = ids.subList(i, Math.min(i + BATCH, ids.size()));
            transactionHelper.runInANewTransaction(() -> {
                updateEmployeeTimeslotBatchTransactional(sub, timeslotMinutes, timeSlot);
            });
        }
        event.setStatus(EventStatus.COMPLETED.name());
        logger.info("UPDATE THE TIMESLOT FOR COMPANY -> {} IS FINISHED", SecurityContext.getCompanyID());
    }

    public void updateEmployeeTimeslotBatchTransactional(List<Integer> sub, EdsTimeSlotItem[] timeslotMinutes, EdsTimeSlot timeSlot) {
        for (Integer id : sub) {
            updateEmployeeTimeslot(id, timeslotMinutes, timeSlot);
        }
    }

    public void updateEmployeeTimeslot(Integer employeeId,
                                       EdsTimeSlotItem[] timeSlotMinutes,
                                       EdsTimeSlot timeSlot) {

        EdsEmployee employee = employeeManager.get(employeeId);

        Date effectiveDate = employee.getUserDate(timeSlot.getEffectiveFrom());
        Date lastDate = attendanceRawDataManager.getLastEnteredDateForEmployee(employeeId);

        Calendar from = atStartOfDay(effectiveDate);
        Calendar to = atStartOfDay(ServerUtils.addDays(effectiveDate, 730));

        Map<Date, Integer> exceptionalMinutes = new HashMap<>();
        if (timeSlot.getExceptionalTimeSlotItem() != null) {
            for (Map.Entry<Date, EdsTimeSlotItem> e : timeSlot.getExceptionalTimeSlotItem().entrySet()) {
                exceptionalMinutes.put(atStartOfDay(e.getKey()).getTime(), e.getValue().getMinutes());
            }
        }

        int[] minutesByCalendarDow = new int[8];
        for (int dow = Calendar.SUNDAY; dow <= Calendar.SATURDAY; dow++) {
            int arrayIndex = (dow == Calendar.SUNDAY) ? 6 : (dow - 2); // Monday=0...Sunday=6
            minutesByCalendarDow[dow] = timeSlotMinutes[arrayIndex].getMinutes();
        }

        boolean needToUpdateLRDetails = false;
        Date updateLRFrom = from.getTime();

        boolean noRecordsThisYear = (lastDate == null)
                || !lastDate.after(ServerUtils.getEndDate(employee.getCompany(), -1));

        if (noRecordsThisYear) {
            List<EdsDate> dates = dateManager.getDatesByDates(from.getTime(), to.getTime());
            logger.info(">>Post Processor>> {} days are about to be created for employee {}", dates.size(), employeeId);

            List<EdsAttendanceRawData> existing =
                    attendanceRawDataManager.getAttendanceRawDataByDates(from.getTime(), to.getTime(), employee.getObjectID());
            Map<Date, EdsAttendanceRawData> byDate = new HashMap<>(existing.size() * 2);
            for (EdsAttendanceRawData ard : existing) {
                byDate.put(atStartOfDay(ard.getDate()).getTime(), ard);
            }

            List<EdsAttendanceRawData> batch = new ArrayList<>(dates.size());
            Calendar cursor = (Calendar) from.clone();
            for (EdsDate d : dates) {
                Date day = atStartOfDay(d.getFromDate()).getTime();
                cursor.setTime(day);

                EdsAttendanceRawData ard = byDate.getOrDefault(day, new EdsAttendanceRawData());
                ard.setDate(day);

                int minutes = exceptionalMinutes.getOrDefault(day, minutesByCalendarDow[cursor.get(Calendar.DAY_OF_WEEK)]);
                ard.setTimeSlot(minutes);
                ard.setDayOff(minutes <= 0);

                ard.setEmployee(employee);
                batch.add(ard);
            }

            attendanceRawDataManager.updateAll(batch, batch.size());
            logger.info(">>Post Processor>> {} days for employee {} finished", dates.size(), employeeId);
        } else {
            logger.info("<<Post Processor<< timeslots are about to be updated for employee {}", employee.getObjectID());

            attendanceRawDataManager.updateAttendanceRawDataByEmployeeAndTimeslotId(
                    employee.getObjectID(), from.getTime(), to.getTime());

            needToUpdateLRDetails = true;
            logger.info("<<Post Processor<< days were updated");
        }

        if (needToUpdateLRDetails) {
            logger.info("<<Post Processor<< sick requests are about to be updated for employee {}", employee.getObjectID());
            availabilityServiceLocal.updateLRHours(employee, updateLRFrom, to.getTime());
            logger.info("<<Post Processor<< sick requests were updated");
        }
    }

    private static Calendar atStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsEmployee employee = employeeManager.get(event.getEntityID());
        EdsTimeSlot edsTimeSlot = timeSlotManager.get(event.getSourceID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerTimeSlotDeleteUpdate(edsTimeSlot, employee, event.getTime());
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
    public void onEditEvent(EdsBusinessEvent event) {
        EdsEmployee employee = employeeManager.get(event.getEntityID());
        EdsTimeSlot edsTimeSlot = timeSlotManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerTimeSlotEditUpdate(edsTimeSlot, employee, event.getTime());
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
    public void onCustomEvent(EdsBusinessEvent event) {
        EdsEmployee employee = employeeManager.get(event.getEntityID());
        EdsTimeSlot edsTimeSlot = timeSlotManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerTimeSlotAddUpdate(edsTimeSlot, employee, event.getTime());
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
}