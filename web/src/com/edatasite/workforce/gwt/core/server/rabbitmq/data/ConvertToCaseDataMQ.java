package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import java.io.Serializable;
import java.util.List;

public class ConvertToCaseDataMQ implements Serializable {
    Integer emailSettingId;
    List<String> emails;

    public Integer getEmailSettingId() {
        return emailSettingId;
    }

    public void setEmailSettingId(Integer emailSettingId) {
        this.emailSettingId = emailSettingId;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
