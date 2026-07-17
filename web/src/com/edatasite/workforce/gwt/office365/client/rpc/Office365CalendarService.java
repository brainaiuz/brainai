package com.edatasite.workforce.gwt.office365.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by umidbekkarimov on 11/23/15.
 */
public interface Office365CalendarService extends RemoteService {


    void saveCalendarSettings(Office365CalendarSettings settings);

    void saveToken(Office365AccessTokenDTO token);

    ArrayList<Appointment> syncEvents(String host, Integer employeeId, Date start, Date end) throws ParseException;

    class App {
        public static Office365CalendarServiceAsync get() {
            ServiceDefTarget target = GWT.create(Office365CalendarService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/office365calendar");
            return (Office365CalendarServiceAsync) target;
        }
    }
}
