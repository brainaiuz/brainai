package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;


public class SuggestionResponseDTO implements IsSerializable, Serializable {
    private static final long serialVersionUID = -6182916410094709183L;

    private String description;
    private Integer time;

    public SuggestionResponseDTO() {
    }

    public SuggestionResponseDTO(String description, Integer time) {
        this.description = description;
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
