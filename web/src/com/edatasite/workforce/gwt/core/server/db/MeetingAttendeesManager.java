package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMeetingAttendees;

import java.util.List;

/**
 * User: developer
 * Date: 4/30/12
 * Time: 12:54 PM
 */
public interface MeetingAttendeesManager extends Manager<EdsMeetingAttendees> {
    List<EdsMeetingAttendees> getMeetingAttendesMeetingId(Integer projectId);

    EdsMeetingAttendees getMeetingAttendeeByMeetingIDAndEmployeeID(Integer meetingMinutesID, Integer employeeID);

    void deleteMeetingAttendees(Integer meetingID);
}