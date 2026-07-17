package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityCircularResolver;
import com.edatasite.workforce.gwt.availability.server.pojo.Holiday;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * User: Anvarbek
 * Date: Dec 27, 2009
 * Time: 4:02:10 PM
 */
@Transactional
public class TaskEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsTask> TYPE = new WfmType<>(EventTypes.taskEventListener);
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private AvailabilityCircularResolver availabilityCircularResolver;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        EdsTask edsTask = taskManager.get(event.getEntityID());
        List<EdsProjectEmployee> newTaskEmployees = taskManager.getTaskAssignees(edsTask.getObjectID());

        String jsString = event.getCustomStringField();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = (JSONArray) new JSONParser().parse(jsString);
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
            return;
        }
        EdsUser user = edsTask.getCreator();
        if (user == null) {
            System.out.println("Task without creator! " + edsTask.getObjectID());
            return;
        }
        boolean isEnableResourceUtilization = false;
        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);
        if (resourcePlanning != null) {
            isEnableResourceUtilization = true;
        }

        Set<EdsEmployeeTask> empTaskSet = edsTask.getUnDeletedAssignments();
        for (EdsEmployeeTask empTask : empTaskSet) {

            EdsProjectEmployee edsProjectEmployee = empTask.getProjectEmployee();
            EdsEmployee edsEmployee = edsProjectEmployee.getEmployeeDepartment().getEmployee();
            Set<EdsTimeSlotItem> timeSlotItem = edsEmployee.getTimeSlot().getItems();
            Map<Integer, Integer> available = new HashMap<>();
            for (EdsTimeSlotItem item : timeSlotItem) {
                available.put(item.getDay(), item.getEndTime() - item.getStartTime());
            }
            if (edsTask.getStartDate() != null && edsTask.getDueDate() != null) {
                String from = null;
                Boolean startResourceCalculationForNewAssigneesFromToday = null;
                if (isEnableResourceUtilization && newTaskEmployees != null && newTaskEmployees.size() > 0) {
                    boolean isTaskOldAssignee = false;
                    //change estimate time
                    try {
                        jsonArray = (JSONArray) new JSONParser().parse(jsString);
                        for (JSONObject aJsonArray : (Iterable<JSONObject>) jsonArray) {
                            JSONObject jsonObject = aJsonArray;
                            if (((Number) jsonObject.get("id")).intValue() == (edsProjectEmployee.getObjectID())) {
                                isTaskOldAssignee = true;
                                if (jsonObject.get("changeEstimate").equals(true)) {
                                    from = AvailabilityCircularResolver.FROM_RESOURCE_UTIL;
                                }
                                if (jsonObject.get("startResourceCalculateAssigneeFromToday") != null) {
                                    startResourceCalculationForNewAssigneesFromToday = (Boolean) jsonObject.get("startResourceCalculateAssigneeFromToday");
                                }
                                break;
                            }
                        }
                    } catch (org.json.simple.parser.ParseException e) {
                        e.printStackTrace();
                    }
                    if (!isTaskOldAssignee) {
                        continue;
                    }
                }

                Calendar startDate = new GregorianCalendar(user.getUserTimezone());
                Calendar dueDate = new GregorianCalendar(user.getUserTimezone());
                long startLongTime = startResourceCalculationForNewAssigneesFromToday != null ? (startResourceCalculationForNewAssigneesFromToday ? new Date().getTime() : edsTask.getStartDate().getTime()) : edsTask.getStartDate().getTime();
                startDate.setTime(new Date(startLongTime + user.getUserTimezone().getRawOffset()));
                dueDate.setTime(new Date(edsTask.getDueDate().getTime() + user.getUserTimezone().getRawOffset()));
                int availableDayCount = 0;

                ArrayList<Holiday> companyHolidays = availabilityCircularResolver.getCompanyHolidayList(user);
                ArrayList<Holiday> employeeHolidays = availabilityCircularResolver.getHolidaysList(edsEmployee);
                ArrayList<Calendar> employeeLeaves = availabilityCircularResolver.getEmployeeLeaves(startDate.getTime(), dueDate.getTime(), edsEmployee);

                ArrayList<Calendar> availableDays = new ArrayList<>();

                Calendar start = Calendar.getInstance();
                start.setTime(startDate.getTime());
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MILLISECOND, 0);

                while (dueDate.getTime().compareTo(start.getTime()) >= 0) {
                    Integer dayIndex = start.get(Calendar.DAY_OF_WEEK) - 1;

                    if (available.containsKey(dayIndex) && available.get(dayIndex) != null && available.get(dayIndex) != 0
                            && !(availabilityCircularResolver.isHoliday(start, companyHolidays) != null || availabilityCircularResolver.isHoliday(start, employeeHolidays) != null)
                            && !(availabilityCircularResolver.isLrDay(start, employeeLeaves))) {
                        availableDayCount++;
                        Calendar nonDate = Calendar.getInstance();
                        nonDate.setTime(start.getTime());
                        ServerUtils.setBeginningOfTheDay(nonDate);
                        availableDays.add(nonDate);
                    }
                    start.add(Calendar.DAY_OF_MONTH, 1);
                }
                if (availableDays.isEmpty()) {//availableDays yo'q bolsa task end datega estimatelarni set qilishga kelishildi
                    Calendar nonDate = Calendar.getInstance();
                    nonDate.setTime(dueDate.getTime());
                    ServerUtils.setBeginningOfTheDay(nonDate);
                    availableDays.add(nonDate);
                }

                Integer timesheetCount = null;
                if (empTask.getEstimatedTime() != null && empTask.getEstimatedTime() == 0) {
                    //vaqt oraligida, from dan to gacha timesheetlar countini olib chiq
                    timesheetCount = timeSheetManager.getTimesheetCount(empTask, start.getTime(), dueDate.getTime());
                }
                //agar timesheetlar counti availableDays bilan bir xil bolsa
                if (timesheetCount != null && availableDayCount == timesheetCount) {
                    //from dan to gacha timesheetlarni daily estimatini 0 ga update qil
                    timeSheetManager.updateDailyEstimatedTime(empTask.getObjectID(), start.getTime(), dueDate.getTime(), 0);
                } else {
                    if (availableDayCount == 0) {
                        availableDayCount = 1;
                    }
                    int estTime = 0;
                    if (empTask.getEstimatedTime() != null) {
                        estTime = empTask.getEstimatedTime();
                    }
                    int dailyLoad = estTime / availableDayCount;
                    int dailyLoadQ = estTime % availableDayCount;
                    empTask.setDailyLoad(dailyLoad);

                    //insert timeSheet data with daily estimated time
                    if (dailyLoad >= 0 && isEnableResourceUtilization && from != null) {
                        from = AvailabilityCircularResolver.FROM_RESOURCE_UTIL;
                        timeSheetManager.updateDailyEstimatedTime(empTask.getObjectID());
                        //update call by from to date, except the first date
                        availabilityCircularResolver.createOrUpdateTimeSheetDataWithDailyEstimatedTime(edsEmployee, empTask, availableDays, dailyLoad, dailyLoadQ, from);
                    }
                }
            }
        }
        event.setStatus(EventStatus.COMPLETED.name());
        System.out.print("********CREATE OR UPDATE TIMESHEET complated******");
    }

    public void onAddEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isSolrIndexed()) {
            try {
                taskSolrComponent.index(task);
                event.setSolrIndexed(true);
            } catch (Exception e) {
                event.setSolrIndexed(false);
            }
        }
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = taskManager.registerTaskAllUpdates(task, creator, event.getTime(), EdsMyUpdate.ADD);
                if (myUpdate != null) {
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemAdd(true);
            } catch (Exception e) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed() && event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        EdsUser updater = userManager.get(event.getSourceID());

        if (!event.isSolrIndexed()) {
            try {
                taskSolrComponent.index(task);
                event.setSolrIndexed(true);
            } catch (Exception e) {
                event.setSolrIndexed(false);
            }
        }
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = taskManager.registerTaskAllUpdates(task, updater, event.getTime(), EdsMyUpdate.EDIT);
                if (myUpdate != null) {
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }

        if (event.isSolrIndexed() && event.isMyUpdatesItemEdit() && event.isSendMail1() && event.isSendMail2()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        EdsUser deleter = userManager.get(event.getSourceID());
        if (!event.isSolrIndexed()) {
            try {
                solrManager.removeTask(task, deleter.getCompany());
                event.setSolrIndexed(true);
            } catch (Exception e) {
                event.setSolrIndexed(false);
            }
        }
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = taskManager.registerTaskAllUpdates(task, deleter, event.getTime(), EdsMyUpdate.DELETE);
                if (myUpdate != null) {
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemDelete(true);
            } catch (Exception e) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed() && event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

    }
}
