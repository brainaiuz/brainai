package com.edatasite.workforce.gwt.core.server.app.social.zoom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoomMeetingSettings {
    private String auto_recording;

    public ZoomMeetingSettings(String auto_recording) {
        this.auto_recording = auto_recording;
    }

    public ZoomMeetingSettings() {
    }

    public String getAuto_recording() {
        return auto_recording;
    }

    public void setAuto_recording(String auto_recording) {
        this.auto_recording = auto_recording;
    }
}
