package com.edatasite.workforce.gwt.googlecalendar.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;

public interface GoogleCalendarServiceAsync {

    void validateCurrentUser(AsyncCallback<Boolean> callback);

    void getWorkforceTrackEvents(Date startDate, AsyncCallback<WorkforceEvents> callback);

    void getCalendarEvents(CalendarFilter filter, AsyncCallback<ArrayList<Appointment>> callback);

    void getCalendarProjects(ArrayList<Integer> employeeIDs, Date start, Date end, AsyncCallback<ArrayList<Appointment>> callback);

    void getCalendarTasks(ArrayList<Integer> employeeIDs, Date start, Date end, boolean fromAgenda, boolean visible, boolean forPDF, AsyncCallback<ArrayList<Appointment>> callback);

    void getCalendarIssues(Date start, Date end, AsyncCallback<ArrayList<Appointment>> callback);

    void getCalendarCourses(CalendarFilter filter, AsyncCallback<ArrayList<Appointment>> callback);

    void getCalendarLeaveRequests(ArrayList<Integer> employeeIDs, Date start, Date end, AsyncCallback<ArrayList<Appointment>> callback);

    void getCalendarHolidays(Date start, Date end, AsyncCallback<ArrayList<Appointment>> callback);

    void getCalendarTasksAndEvents(ArrayList<Integer> employeeID, Date start, Date end, boolean eventVisible, boolean taskVisible, AsyncCallback<ArrayList<Appointment>> callback);

    void getCompanyEmployeesWithTeams(AsyncCallback<ArrayList<TeamEmployees>> callback);

    void getAvailableCompanyEmployeesWithTeams(Date startDate, Date endDate, AsyncCallback<ArrayList<TeamEmployees>> callback);

    void getCompanyEmployees(AsyncCallback<SelectItem[]> callback);

    void shareEventToEmployees(int eventID, ArrayList<Attendee> attendees, boolean forceToShare, AsyncCallback<ArrayList<SelectItem>> callback);

    void deleteEvent(Integer employeeID, Integer eventID, String deleteType, boolean deleteWithNotify, AsyncCallback<Boolean> callback);

    void synchronizeEvents(Integer employeeId, Date start, Date end, AsyncCallback<ArrayList<Appointment>> callback);

    void getEmployeesIDByEvent(Integer eventID, AsyncCallback<ArrayList<Integer>> callback);

    void getConflictedEmployees(ArrayList<Attendee> attendees, Date start, Date end, Integer eventID, AsyncCallback<ArrayList<SelectItem>> callback);

    void getReminders(Integer objectID, boolean isEvent, AsyncCallback<ArrayList<CalendarEventReminder>> callback);

    void getEventSharedEmployees(Integer eventID, String creator, AsyncCallback<String> callback);

    void getPriorities(AsyncCallback<SelectItem[]> callback);

    void deleteTask(Integer taskID, String deleteType, AsyncCallback<Boolean> callback);

    void saveCalendarTask(Appointment newTask, AsyncCallback<SelectItem> callback);

    void getAssigneesWithPositions2(Integer projectID, AsyncCallback<LinkedList<WfmTreeItem>> callback);

    void getOnlyAvailableAssigneesWithPosition1(Integer projectID, Date startDate, Date endDate, AsyncCallback<LinkedList<WfmTreeItem>> callback);

    void saveCalendarEvent(Integer employeeId, Appointment appointment, boolean withNotify, AsyncCallback<SelectItem> callback);

    void isAssigneeOnHoliday(ArrayList<Attendee> attendee, Date startDate, Date endDate, boolean isAllDay, AsyncCallback<String> callback);

    void saveCalendarSettings(UsersCalendarSettingsItem calendarSettingsItem, AsyncCallback<UsersCalendarSettingsItem> callback);

    void getTaskAssignees(Integer taskId, AsyncCallback<ArrayList<IdTime>> callback);

    void getEventAttachments(Integer eventID, AsyncCallback<FileResource[]> callback);

    void getAppointment(Integer objectID, boolean isCopy, AsyncCallback<Appointment> asyncCallback);

    void getLocationAsSelectItem(AsyncCallback<SelectItem[]> async);

    void saveCalendarSyncSettings(boolean syncFromDefaultCalendar, AsyncCallback<Void> async);

    void getSelectedEmployees(AsyncCallback<ArrayList<Integer>> callback);

    void syncEvents(String host, Integer userId, Date start, Date end, AsyncCallback<ArrayList<Appointment>> callback);

    void saveCalendarEvent(Integer userID, Appointment appointment, boolean forSync, boolean withNotify, boolean isSolrIndex, HashSet<Integer> objectIDs, Integer reminderRecurrenceID, AsyncCallback<ArrayList<Integer>> async);

    void getTimeZones(AsyncCallback<ArrayList<SelectItem>> callback);

    void saveOfficeCalendarTimeZone(Integer selectedId, AsyncCallback<Boolean> callback);

    void getSelectedTimeZone(AsyncCallback<Integer> callback);

}
