package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public class EmployeeSolr {

    private Integer id;
    private String name;
    private SelectItem team;
    private SelectItem position;
    private SelectItem location;
    private SelectItem supervisor;

    public EmployeeSolr(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public SelectItem getTeam() {
        return team;
    }

    public void setTeam(SelectItem team) {
        this.team = team;
    }

    public SelectItem getPosition() {
        return position;
    }

    public void setPosition(SelectItem position) {
        this.position = position;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SelectItem supervisor) {
        this.supervisor = supervisor;
    }
}
