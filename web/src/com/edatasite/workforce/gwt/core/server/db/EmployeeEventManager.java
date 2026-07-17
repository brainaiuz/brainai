/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/19 3:56:10                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Oct 20, 2009
 * Time: 4:36:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmployeeEventManager extends Manager<EdsEmployeeEvent> {

    List<EdsEmployeeEvent> getCalendarEvents(List<Integer> employeeIDs, Date start, Date end, boolean fromAgenda, Integer locationID, boolean hideCalls);

    List<EdsEmployeeEvent> getCalendarEvents(List<Integer> employeeIDs, Date start, Date end, boolean fromAgenda, Integer locationID, boolean hideCalls, boolean isCall);

    List<Integer> getCalendarEventIDs(List<Integer> employeeIDs, Date start, Date end, boolean fromAgenda, Integer locationID, boolean hideCalls, boolean isCall);

    EdsEmployeeEvent getEmployeeEvent(EdsUser employee, EdsEvent event);

    List<Integer> getEventRelatedEmployees(Integer eventID);

    String getEventEmployees(Integer eventID);

    String getEventRelatedEmployeesEmails(Integer eventID);

    List<Integer> getUnavailableEmployeeIDs(EdsCompany company, Date startDate, Date endDate);

    EdsEmployeeEvent hasConflictedEvents(Integer eventID, EdsEmployee employee, Date start, Date end);

    List<EdsEmployeeEvent> getEmployeeAllEvents(EdsEmployee employee);

    List<EdsEmployeeEvent> getEmployeeAllEvents(EdsEmployee employee, Boolean withRecurrence);

    void removeGoogleIDFromEmployeeEvents(EdsEmployee employee);

    List<EdsUser> getEventAttendees(EdsEvent event);

    EdsEmployeeEvent getEmployeeEventByEvent(EdsEvent event);

    List<EdsUser> getEventSharedEmployees(Integer eventID);

    String getEmployeeAllEventsCount(EdsEmployee employee);

    void deleteEmployeeEvents(EdsEvent event);

    List<EdsEmployeeEvent> getEmployeeEvents(Integer eventID);

    void setEmployeeEventsModifiedDate(EdsEvent event, Date lastModifiedDate);

    void removeOfficeIDFromEmployeeEvents(EdsEmployee employee);

    EdsEmployeeEvent getByOfficeID(String id);

    void removeOfficeIDFromEvents(EdsEmployee employee);
}
