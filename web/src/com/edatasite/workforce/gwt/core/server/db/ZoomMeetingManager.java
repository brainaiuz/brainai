package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsZoomMeeting;

public interface ZoomMeetingManager extends Manager<EdsZoomMeeting> {
   EdsZoomMeeting getMeetingByEventId(int eventId);

   EdsZoomMeeting getMeetingByEventUrl(String url);

   void deleteMeetingById(long meetingId);
}
