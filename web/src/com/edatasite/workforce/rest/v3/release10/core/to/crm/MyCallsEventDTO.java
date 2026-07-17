package com.edatasite.workforce.rest.v3.release10.core.to.crm;

public class MyCallsEventDTO {
    private int event_type;
    private long event_created;
    private int direction;
    private String client_number;
    private String client_name;
    private String event_pbx_call_id;
    private long start_time;
    private long answer_time;
    private long end_time;
    private int duration;
    private int answered;
    private String recording;
    private String db_call_id;

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }

    public long getEvent_created() {
        return event_created;
    }

    public void setEvent_created(long event_created) {
        this.event_created = event_created;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getClient_number() {
        return client_number;
    }

    public void setClient_number(String client_number) {
        this.client_number = client_number;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getEvent_pbx_call_id() {
        return event_pbx_call_id;
    }

    public void setEvent_pbx_call_id(String event_pbx_call_id) {
        this.event_pbx_call_id = event_pbx_call_id;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(long answer_time) {
        this.answer_time = answer_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAnswered() {
        return answered;
    }

    public void setAnswered(int answered) {
        this.answered = answered;
    }

    public String getRecording() {
        return recording;
    }

    public void setRecording(String recording) {
        this.recording = recording;
    }

    public String getDb_call_id() {
        return db_call_id;
    }

    public void setDb_call_id(String db_call_id) {
        this.db_call_id = db_call_id;
    }
}
