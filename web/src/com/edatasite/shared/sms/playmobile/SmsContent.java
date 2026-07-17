package com.edatasite.shared.sms.playmobile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsContent {
    private String text;

    public SmsContent() {
    }

    public SmsContent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmsContent)) return false;

        SmsContent that = (SmsContent) o;

        if (getText() != null ? !getText().equals(that.getText()) : that.getText() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getText() != null ? getText().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SmsContent{" +
                "text='" + text + '\'' +
                '}';
    }
}
