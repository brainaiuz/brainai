package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMeetingAttendees;
import com.edatasite.workforce.gwt.core.server.db.MeetingAttendeesManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: developer
 * Date: 4/30/12
 * Time: 12:55 PM
 */
@Repository("meetingAttendeesManager")
public class MeetingAttendeesManagerImpl extends AttachmentSupportManager<EdsMeetingAttendees> implements MeetingAttendeesManager {

    public MeetingAttendeesManagerImpl() {
        super(EdsMeetingAttendees.class);
    }

    @Override
    public List<EdsMeetingAttendees> getMeetingAttendesMeetingId(Integer meetingminutesid) {
        return find("select mm from EdsMeetingAttendees mm where mm.meetingMinutes.objectID=?", meetingminutesid);
    }

    public EdsMeetingAttendees getMeetingAttendeeByMeetingIDAndEmployeeID(Integer meetingMinutesID, Integer employeeID) {
        return (EdsMeetingAttendees) findSingle("SELECT ma FROM EdsMeetingAttendees ma WHERE ma.meetingMinutes.objectID=? AND ma.attendeesEmployee.objectID=?", meetingMinutesID, employeeID);
    }

    @Override
    public void deleteMeetingAttendees(Integer meetingID) {
        updateNative("delete from " + getCompanyId() + ".meetingattendees where meetingminutesid=" + meetingID.toString());
    }
}