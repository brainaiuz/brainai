package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.ArrayList;
import java.util.List;

public class PredefinedValueItem extends SelectItem {
    private ArrayList<SelectItem> viewRoles;
    private ArrayList<SelectItem> viewEmployees;
    private ArrayList<SelectItem> changeRoles;
    private ArrayList<SelectItem> changeEmployees;
    private ArrayList<SelectItem> allRoles;

    public PredefinedValueItem() {
    }

    public PredefinedValueItem(Integer id, String name) {
        super(id, name);
    }

    public ArrayList<SelectItem> getViewRoles() {
        return viewRoles;
    }

    public void setViewRoles(List<SelectItem> viewRoles) {
        this.viewRoles = new ArrayList<>(viewRoles);
    }

    public ArrayList<SelectItem> getViewEmployees() {
        return viewEmployees;
    }

    public void setViewEmployees(List<SelectItem> viewEmployees) {
        this.viewEmployees = new ArrayList<>(viewEmployees);
    }

    public ArrayList<SelectItem> getChangeRoles() {
        return changeRoles;
    }

    public void setChangeRoles(List<SelectItem> changeRoles) {
        this.changeRoles = new ArrayList<>(changeRoles);
    }

    public ArrayList<SelectItem> getChangeEmployees() {
        return changeEmployees;
    }

    public void setChangeEmployees(List<SelectItem> changeEmployees) {
        this.changeEmployees = new ArrayList<>(changeEmployees);
    }

    public ArrayList<SelectItem> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(ArrayList<SelectItem> allRoles) {
        this.allRoles = allRoles;
    }
}
