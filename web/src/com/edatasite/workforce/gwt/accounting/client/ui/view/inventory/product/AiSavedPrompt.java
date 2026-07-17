package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product;

import java.io.Serializable;

public class AiSavedPrompt implements Serializable {

    private Integer id;
    private String text;

    public AiSavedPrompt() {
    }

    public AiSavedPrompt(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
