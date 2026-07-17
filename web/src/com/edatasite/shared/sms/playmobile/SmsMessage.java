package com.edatasite.shared.sms.playmobile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsMessage {
    private String recipient;
    @JsonProperty("message-id")
    private String messageId;
    private SmsContext sms;

    public SmsMessage() {
    }

    public SmsMessage(String recipient, String messageId, SmsContext sms) {
        this.recipient = recipient;
        this.messageId = messageId;
        this.sms = sms;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public SmsContext getSms() {
        return sms;
    }

    public void setSms(SmsContext sms) {
        this.sms = sms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmsMessage)) return false;

        SmsMessage that = (SmsMessage) o;

        if (getRecipient() != null ? !getRecipient().equals(that.getRecipient()) : that.getRecipient() != null)
            return false;
        if (getMessageId() != null ? !getMessageId().equals(that.getMessageId()) : that.getMessageId() != null)
            return false;
        if (getSms() != null ? !getSms().equals(that.getSms()) : that.getSms() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRecipient() != null ? getRecipient().hashCode() : 0;
        result = 31 * result + (getMessageId() != null ? getMessageId().hashCode() : 0);
        result = 31 * result + (getSms() != null ? getSms().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SmsMessage{" +
                "recipient='" + recipient + '\'' +
                ", messageId='" + messageId + '\'' +
                ", sms=" + sms +
                '}';
    }
}
