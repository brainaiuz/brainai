package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsZoomMeeting;
import com.edatasite.workforce.gwt.core.server.db.ZoomMeetingManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("zoomMeetingManager")
public class ZoomMeetingManagerImpl extends BaseManager<EdsZoomMeeting> implements ZoomMeetingManager {
    public ZoomMeetingManagerImpl() {
        super(EdsZoomMeeting.class);
    }

    public EdsZoomMeeting getMeetingByEventId(int eventId) {
        Object nativeSingle = findNativeSingle("select z.* from" + getCompanyId() + ".zoom_meeting z where z.event_id = " + eventId, EdsZoomMeeting.class);
        return (EdsZoomMeeting) nativeSingle;
    }

    @Override
    public EdsZoomMeeting getMeetingByEventUrl(String url) {
        return (EdsZoomMeeting) findNativeSingle("select * from " + getCompanyId() + ".zoom_meeting  where join_url = '" + url + "'", EdsZoomMeeting.class);
    }

    public void deleteMeetingById(long meetingId) {
        updateNative("update " + getCompanyId() + ".zoom_meeting set is_deleted = true where meetingId = " + meetingId);
    }
}
