package com.edatasite.workforce.gwt.employee.client.rpc;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SupervisorLevelResponse implements IsSerializable {
    private String orgChart;
    private LinkedHashMap<Integer, ArrayList<EdsEmployee>> map;
    private ArrayList<EdsEmployee> employees;

    public String getOrgChart() {
        return orgChart;
    }

    public void setOrgChart(String orgChart) {
        this.orgChart = orgChart;
    }

    public ArrayList<EdsEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<EdsEmployee> employees) {
        this.employees = employees;
    }
}
