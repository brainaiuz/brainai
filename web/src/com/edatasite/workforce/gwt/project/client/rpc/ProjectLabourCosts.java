package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 15.11.2008
 * Time: 17:02:02
 * To change this template use File | Settings | File Templates.
 */
public class ProjectLabourCosts extends WfmTreeItem {

    private String[] assignees;
    private int estimatedTime;
    private int timspent;

    private double wageAmmount;
    private double clientWageAmmount;

    private double plannedWageAmount;
    private double plannedClientChargeAmount;

    private double actualWageAmount;
    private double actualClientChargeAmount;

    public static final int WORKSTREAM = 1000;
    public static final int TASK = 1;

    public ProjectLabourCosts(Integer id, String name, int nodeType) {
        super(id, name);
        this.nodeType = nodeType;
    }

    public ProjectLabourCosts() {

    }

    private int nodeType;

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public int getNodeType() {
        return nodeType;
    }


    public String[] getAssignees() {
        return assignees;
    }

    public void setAssignees(String[] assignees) {
        this.assignees = assignees;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public String getWageAmmountString() {
        return Utils.formatDouble(wageAmmount);
    }

    public String getClientChargeAmmountString() {
        return Utils.formatDouble(clientWageAmmount);
    }


    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getTimspent() {
        return timspent;
    }

    public void setTimspent(int timspent) {
        this.timspent = timspent;
    }

    public double getWageAmmount() {
        return wageAmmount;
    }

    public void setWageAmmount(double wageAmmount) {
        this.wageAmmount = wageAmmount;
    }

    public double getClientWageAmmount() {
        return clientWageAmmount;
    }

    public void setClientWageAmmount(double clientWageAmmount) {
        this.clientWageAmmount = clientWageAmmount;
    }

    public double getPlannedWageAmount() {
        return plannedWageAmount;
    }

    public void setPlannedWageAmount(double plannedWageAmount) {
        this.plannedWageAmount = plannedWageAmount;
    }

    public double getPlannedClientChargeAmount() {
        return plannedClientChargeAmount;
    }

    public void setPlannedClientChargeAmount(double plannedClientChargeAmount) {
        this.plannedClientChargeAmount = plannedClientChargeAmount;
    }

    public double getActualWageAmount() {
        return actualWageAmount;
    }

    public void setActualWageAmount(double actualWageAmount) {
        this.actualWageAmount = actualWageAmount;
    }

    public double getActualClientChargeAmount() {
        return actualClientChargeAmount;
    }

    public void setActualClientChargeAmount(double actualClientChargeAmount) {
        this.actualClientChargeAmount = actualClientChargeAmount;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectLabourCosts)) {
            return false;
        }
        ProjectLabourCosts other = (ProjectLabourCosts) o;
        if (getNodeType() != other.getNodeType()) {
            return false;
        }
        return getId().intValue() == other.getId().intValue();
    }


    public int hashCode() {
        return getId() + nodeType;
    }


}
