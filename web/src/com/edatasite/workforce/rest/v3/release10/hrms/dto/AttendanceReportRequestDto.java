package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.enums.AttendanceGroupBy;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * User : Akhror on 03/03/2023
 */
public class AttendanceReportRequestDto {
    @NotNull(message = "period is required")
    private String period;
    private ArrayList<ItemDto> departments;
    private String searchText;
    private ArrayList<ItemDto> reasons;
    private ItemDto location;
    private ItemDto position;
    private ItemDto timeslot;
    private AttendanceGroupBy groupBy;
    private int start = 0;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public ArrayList<ItemDto> getDepartments() {
        return departments;
    }

    public void setDepartments(ArrayList<ItemDto> departments) {
        this.departments = departments;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public ArrayList<ItemDto> getReasons() {
        return reasons;
    }

    public void setReasons(ArrayList<ItemDto> reasons) {
        this.reasons = reasons;
    }

    public ItemDto getLocation() {
        return location;
    }

    public void setLocation(ItemDto location) {
        this.location = location;
    }

    public ItemDto getPosition() {
        return position;
    }

    public void setPosition(ItemDto position) {
        this.position = position;
    }

    public ItemDto getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(ItemDto timeslot) {
        this.timeslot = timeslot;
    }

    public AttendanceGroupBy getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(AttendanceGroupBy groupBy) {
        this.groupBy = groupBy;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
