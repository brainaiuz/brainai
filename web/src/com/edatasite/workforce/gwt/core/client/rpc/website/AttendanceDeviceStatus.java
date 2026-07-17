package com.edatasite.workforce.gwt.core.client.rpc.website;

import java.io.Serializable;
import java.util.ArrayList;

public class AttendanceDeviceStatus implements Serializable {
    private Long id;
    private Boolean active;
    private String status;
    private TerminalTime time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TerminalTime getTime() {
        return time;
    }

    public void setTime(TerminalTime time) {
        this.time = time;
    }

    public static class TerminalTime implements Serializable {
        private ArrayList<Integer> value;

        public ArrayList<Integer> getValue() {
            return value;
        }

        public void setValue(ArrayList<Integer> value) {
            this.value = value;
        }
    }
}
