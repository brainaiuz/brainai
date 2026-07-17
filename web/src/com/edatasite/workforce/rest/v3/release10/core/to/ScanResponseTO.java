package com.edatasite.workforce.rest.v3.release10.core.to;

public class ScanResponseTO {
    private boolean active;
    private String status;

    public ScanResponseTO(boolean active, String status) {
        this.active = active;
        this.status = status;
    }

    public ScanResponseTO() {
    }

    public boolean isActive() {
        return active;
    }

    public String getStatus() {
        return status;
    }
}
