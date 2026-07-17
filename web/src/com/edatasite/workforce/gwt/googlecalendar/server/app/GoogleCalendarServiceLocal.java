package com.edatasite.workforce.gwt.googlecalendar.server.app;

import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.WorkforceEvents;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 8/29/11
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleCalendarServiceLocal {

    ArrayList<SelectItem> wrapEdsGoogleCalendarEventGuestsToSelectItem(EdsEvent event, boolean replaceEmailToName);

    void createRecurringEvent();

    Boolean deleteEvent(Integer employeeID, Integer eventID, String deleteType, boolean deleteWithNotify);

    SelectItem saveCalendarEvent(Integer employeeId, Appointment appointment, boolean withNotify);

    ArrayList<Appointment> synchronizeEvents(Integer employeeId, Date start, Date end) throws Exception;

    void sendEventNotification(Integer eventId, Integer reminderType);

    String saveToken(String token) throws Exception;

    EdsUserSession getUserBySession(String sessionId);

    ArrayList<Appointment> syncEvents(String host, Integer userId, Date start, Date end) throws Exception;

    Appointment getAppointment(Integer objectID, boolean isCopy);

    SelectItem saveCalendarTask(TaskSingleItem newTask, Integer userID);

    List<Integer> saveCalendarEvent(Integer user, Appointment appointment, boolean forSync, boolean withNotify, boolean isSolrIndex, HashSet<Integer> objectIDs, Integer reminderRecurrenceID);

    void updateEventGuestStatus(Integer companyID, Integer eventId, String email, String answer);

    WorkforceEvents getWorkforceTrackEventsForPDF(Date start, Date end, boolean forPDF);

    ArrayList<Appointment> getUserOverdueTasks();

    void addEmployeeToEvent(Integer eventId, Integer employeeId);

    Appointment getAppointmentByAsteriskid(String asteriskid);
}
