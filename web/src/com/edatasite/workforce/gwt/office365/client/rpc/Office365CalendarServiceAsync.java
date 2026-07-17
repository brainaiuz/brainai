package com.edatasite.workforce.gwt.office365.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by umidbekkarimov on 11/23/15.
 */
public interface Office365CalendarServiceAsync {


    void saveCalendarSettings(Office365CalendarSettings settings, AsyncCallback<Void> callback);

    void saveToken(Office365AccessTokenDTO token, AsyncCallback<Void> async);

    void syncEvents(String host, Integer userId, Date start, Date end, AsyncCallback<ArrayList<Appointment>> callback) throws ParseException;
}
