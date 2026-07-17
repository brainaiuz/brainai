package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;

/**
 * User: Abror Abdukadirov
 * Date: 25.05.2017 2:50
 */
public class TelegramMessageRequestObject implements Serializable {
    private Long chat_id;
    private String text;

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
