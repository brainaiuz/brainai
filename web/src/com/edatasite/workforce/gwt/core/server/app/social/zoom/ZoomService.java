package com.edatasite.workforce.gwt.core.server.app.social.zoom;

import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.google.gwt.user.client.rpc.RemoteService;

public interface ZoomService extends RemoteService {
    String getAccessToken(String code);

    Appointment createMeeting(EdsEvent event);

    void updateMeeting(EdsEvent event);

    void deleteMeeting(EdsEvent event);
}
