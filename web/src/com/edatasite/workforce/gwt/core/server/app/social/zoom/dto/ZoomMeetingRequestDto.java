package com.edatasite.workforce.gwt.core.server.app.social.zoom.dto;

import com.edatasite.workforce.gwt.core.server.app.social.zoom.model.ZoomMeetingSettings;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoomMeetingRequestDto {
    private String topic;
    private String agenda;
    private String timezone;
    @JsonProperty("start_time")
    private String start_time;
    @JsonProperty("settings")
    private ZoomMeetingSettings settings;
    private int duration;

    private String password;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ZoomMeetingSettings getSettings() {
        return settings;
    }

    public void setSettings(ZoomMeetingSettings settings) {
        this.settings = settings;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
