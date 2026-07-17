package com.edatasite.workforce.gwt.core.server.app.social.zoom.dto;

import com.edatasite.workforce.gwt.core.server.app.social.zoom.model.ZoomMeetingSettings;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoomMeetingResponseDto {

    private long id;
    private String topic;

    private String agenda;

    private int duration;

    private String join_url;

    @JsonProperty("start_time")
    private Date start_time;

    private String timezone;

    private Date created_at;

    private String start_url;
    @JsonProperty("password")
    private String password;

    @JsonProperty("settings")
    private ZoomMeetingSettings settings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getJoin_url() {
        return join_url;
    }

    public void setJoin_url(String join_url) {
        this.join_url = join_url;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getStart_url() {
        return start_url;
    }

    public void setStart_url(String start_url) {
        this.start_url = start_url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ZoomMeetingSettings getSettings() {
        return settings;
    }

    public void setSettings(ZoomMeetingSettings settings) {
        this.settings = settings;
    }
}
