package com.edatasite.shared.sms.playmobile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayMobileRequestDto {
    private List<SmsMessage> messages;

    public PlayMobileRequestDto() {
    }

    public PlayMobileRequestDto(List<SmsMessage> messages) {
        this.messages = messages;
    }

    public List<SmsMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<SmsMessage> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayMobileRequestDto)) return false;

        PlayMobileRequestDto that = (PlayMobileRequestDto) o;

        if (getMessages() != null ? !getMessages().equals(that.getMessages()) : that.getMessages() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getMessages() != null ? getMessages().hashCode() : 0;
    }
}
