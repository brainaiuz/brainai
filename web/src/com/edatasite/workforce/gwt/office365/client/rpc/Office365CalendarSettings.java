package com.edatasite.workforce.gwt.office365.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by umidbekkarimov on 11/23/15.
 */
public class Office365CalendarSettings implements IsSerializable {
    private boolean sync;

    private String taskCalendarId;
    private String eventCalendarId;

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getTaskCalendarId() {
        return taskCalendarId;
    }

    public void setTaskCalendarId(String taskCalendarId) {
        this.taskCalendarId = taskCalendarId;
    }

    public String getEventCalendarId() {
        return eventCalendarId;
    }

    public void setEventCalendarId(String eventCalendarId) {
        this.eventCalendarId = eventCalendarId;
    }
}
