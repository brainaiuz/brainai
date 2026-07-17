package com.edatasite.shared.sms.playmobile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsContext {
    private String originator;
    private SmsContent content;

    public SmsContext() {
    }

    public SmsContext(String originator, SmsContent content) {
        this.originator = originator;
        this.content = content;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public SmsContent getContent() {
        return content;
    }

    public void setContent(SmsContent content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmsContext)) return false;

        SmsContext that = (SmsContext) o;

        if (getOriginator() != null ? !getOriginator().equals(that.getOriginator()) : that.getOriginator() != null)
            return false;
        if (getContent() != null ? !getContent().equals(that.getContent()) : that.getContent() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getOriginator() != null ? getOriginator().hashCode() : 0;
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SmsContext{" +
                "originator='" + originator + '\'' +
                ", content=" + content +
                '}';
    }
}
