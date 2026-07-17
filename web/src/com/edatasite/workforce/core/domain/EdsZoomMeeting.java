package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "zoom_meeting")
public class EdsZoomMeeting extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private long meetingId;
    private String topic;

    private String agenda;

    private int duration;

    @Column(name = "join_url", length = 1000)
    private String joinUrl;

    @Column(name = "start_time")
    private Date startTime;

    private String timezone;

    private Date createdAt;

    @Column(name = "start_url", length = 1000)
    private String startUrl;
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    private EdsEvent event;

    @Column(name = "is_deleted", columnDefinition = "boolean default true")
    private boolean isDeleted;

    private String auto_recording;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(long meetingId) {
        this.meetingId = meetingId;
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

    public String getJoinUrl() {
        return joinUrl;
    }

    public void setJoinUrl(String join_url) {
        this.joinUrl = join_url;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStartUrl() {
        return startUrl;
    }

    public void setStartUrl(String startUrl) {
        this.startUrl = startUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EdsEvent getEventId() {
        return event;
    }

    public void setEventId(EdsEvent eventId) {
        this.event = eventId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getAuto_recording() {
        return auto_recording;
    }

    public void setAuto_recording(String auto_recording) {
        this.auto_recording = auto_recording;
    }
}
