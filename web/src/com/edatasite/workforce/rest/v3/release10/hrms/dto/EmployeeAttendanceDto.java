package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import java.util.ArrayList;

/**
 * User : Akhror on 03/03/2023
 */
public class EmployeeAttendanceDto {
    private Integer id;
    private String name;
    private String code;
    private int workedHours;
    private int workedDays;
    private long plannedHours;
    private int plannedDays;
    private ArrayList<AttendanceDto> attendances;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(int workedHours) {
        this.workedHours = workedHours;
    }

    public int getWorkedDays() {
        return workedDays;
    }

    public void setWorkedDays(int workedDays) {
        this.workedDays = workedDays;
    }

    public long getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(long plannedHours) {
        this.plannedHours = plannedHours;
    }

    public int getPlannedDays() {
        return plannedDays;
    }

    public void setPlannedDays(int plannedDays) {
        this.plannedDays = plannedDays;
    }

    public ArrayList<AttendanceDto> getAttendances() {
        return attendances;
    }

    public void setAttendances(ArrayList<AttendanceDto> attendances) {
        this.attendances = attendances;
    }
}
