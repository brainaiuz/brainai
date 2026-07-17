package com.edatasite.workforce.gwt.core.client.twilio;

import com.google.gwt.core.client.JavaScriptObject;

public final class CallParameters extends JavaScriptObject {
    public enum Status {QUEUED, RINGING, IN_PROGRESS, COMPLETED, BUSY, FAILED, NO_ANSWER}

    public enum Direction {INBOUND, OUTBOUND_DIAL}

    protected CallParameters() {
    }

    private native String getString(String field)
        /*-{
            if (this[field] == undefined)
                return null;
            return this[field];
        }-*/;

    public Status getCallStatus() {
        return Status.valueOf(getString("CallStatus").toUpperCase()
                .replace("-", "_"));
    }

    public Direction getDirection() {
        return Direction.valueOf(getString("Direction").toUpperCase()
                .replace("-", "_"));
    }

    public String getCallSid() {
        return getString("CallSid");
    }

    public String getAccountSid() {
        return getString("AccountSid");
    }

    public String getFrom() {
        return getString("From");
    }

    public String getTo() {
        return getString("To");
    }

    public String getApiVersion() {
        return getString("ApiVersion");
    }

    public String getForwardedFrom() {
        return getString("ForwardedFrom");
    }

    public String getCallerName() {
        return getString("CallerName");
    }

    public String getFromCity() {
        return getString("FromCity");
    }

    public String getFromState() {
        return getString("FromState");
    }

    public String getFromZip() {
        return getString("FromZip");
    }

    public String getFromCountry() {
        return getString("FromCountry");
    }

    public String getToCity() {
        return getString("ToCity");
    }

    public String getToState() {
        return getString("ToState");
    }

    public String getToZip() {
        return getString("ToZip");
    }

    public String getToCountry() {
        return getString("ToCountry");
    }
}
