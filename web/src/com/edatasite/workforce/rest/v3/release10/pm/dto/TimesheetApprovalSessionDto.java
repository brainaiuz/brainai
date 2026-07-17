package com.edatasite.workforce.rest.v3.release10.pm.dto;

import java.util.List;

public class TimesheetApprovalSessionDto {
    private Integer id;
    private List<TimesheetDTO> items;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TimesheetDTO> getItems() {
        return items;
    }

    public void setItems(List<TimesheetDTO> items) {
        this.items = items;
    }
}
