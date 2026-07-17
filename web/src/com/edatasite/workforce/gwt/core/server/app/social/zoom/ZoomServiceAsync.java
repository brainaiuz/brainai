package com.edatasite.workforce.gwt.core.server.app.social.zoom;

import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ZoomServiceAsync {
    void getAccessToken(String code, AsyncCallback<String> callback);

    void createMeeting(EdsEvent event, AsyncCallback<Appointment> callback);

    void updateMeeting(EdsEvent event, AsyncCallback<Void> callback);

    void deleteMeeting(EdsEvent event, AsyncCallback<Void> callback);

}
