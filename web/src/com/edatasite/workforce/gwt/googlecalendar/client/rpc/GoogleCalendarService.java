package com.edatasite.workforce.gwt.googlecalendar.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;

public interface GoogleCalendarService extends RemoteService {

    boolean validateCurrentUser();

    WorkforceEvents getWorkforceTrackEvents(Date startDate);

    ArrayList<Appointment> getCalendarEvents(CalendarFilter filter);

    ArrayList<Appointment> getCalendarProjects(ArrayList<Integer> employeeIDs, Date start, Date end);

    ArrayList<Appointment> getCalendarTasks(ArrayList<Integer> employeeIDs, Date start, Date end, boolean fromAgenda, boolean visible, boolean forPDF);

    ArrayList<Appointment> getCalendarIssues(Date start, Date end);

    ArrayList<Appointment> getCalendarCourses(CalendarFilter filter);

    ArrayList<Appointment> getCalendarLeaveRequests(ArrayList<Integer> employeeIDs, Date start, Date end);

    ArrayList<Appointment> getCalendarHolidays(Date start, Date end);

    ArrayList<Appointment> getCalendarTasksAndEvents(ArrayList<Integer> employeeID, Date start, Date end, boolean eventVisible, boolean taskVisible);

    ArrayList<TeamEmployees> getCompanyEmployeesWithTeams();

    ArrayList<TeamEmployees> getAvailableCompanyEmployeesWithTeams(Date startDate, Date endDate);

    SelectItem[] getCompanyEmployees();

    ArrayList<SelectItem> shareEventToEmployees(int eventID, ArrayList<Attendee> attendees, boolean forceToShare);

    Boolean deleteEvent(Integer employeeID, Integer eventID, String deleteType, boolean deleteWithNotify);

    ArrayList<Appointment> synchronizeEvents(Integer employeeId, Date start, Date end) throws Exception;

    ArrayList<Integer> getEmployeesIDByEvent(Integer eventID);

    ArrayList<SelectItem> getConflictedEmployees(ArrayList<Attendee> attendees, Date start, Date end, Integer eventID);

    ArrayList<CalendarEventReminder> getReminders(Integer objectID, boolean isEvent);

    String getEventSharedEmployees(Integer eventID, String creator);

    SelectItem[] getPriorities();

    SelectItem saveCalendarTask(Appointment newTask);

    Boolean deleteTask(Integer taskID, String deleteType);

    LinkedList<WfmTreeItem> getAssigneesWithPositions2(Integer projectID);

    LinkedList<WfmTreeItem> getOnlyAvailableAssigneesWithPosition1(Integer projectID, Date startDate, Date endDate);

    SelectItem saveCalendarEvent(Integer employeeId, Appointment appointment, boolean withNotify);

    String isAssigneeOnHoliday(ArrayList<Attendee> attendee, Date startDate, Date endDate, boolean isAllDay);

    UsersCalendarSettingsItem saveCalendarSettings(UsersCalendarSettingsItem calendarSettingsItem);

    ArrayList<IdTime> getTaskAssignees(Integer taskId);

    FileResource[] getEventAttachments(Integer eventID);

    SelectItem[] getLocationAsSelectItem();

    void saveCalendarSyncSettings(boolean syncFromDefaultCalendar);

    ArrayList<Integer> getSelectedEmployees();

    ArrayList<Integer> saveCalendarEvent(Integer user, Appointment appointment, boolean forSync, boolean withNotify, boolean isSolrIndex, HashSet<Integer> objectIDs, Integer reminderRecurrenceID);

    ArrayList<Appointment> syncEvents(String host, Integer userId, Date start, Date end);

    ArrayList<SelectItem> getTimeZones();

    Boolean saveOfficeCalendarTimeZone(Integer timeZoneID);

    Integer getSelectedTimeZone();

    Appointment getAppointment(Integer objectID, boolean isCopy);

    class App {
        public static GoogleCalendarServiceAsync get() {
            ServiceDefTarget target = GWT.create(GoogleCalendarService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/googlecalendar");
            return (GoogleCalendarServiceAsync) target;
        }
    }
}
