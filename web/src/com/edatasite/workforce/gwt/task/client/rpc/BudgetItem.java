package com.edatasite.workforce.gwt.task.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.11.2008
 * Time: 14:53:14
 * To change this template use File | Settings | File Templates.
 */
public class BudgetItem implements IsSerializable {

    private String employeeName;
    private String employeePosition;
    private double wageRate;
    private double clientChargeRate;

    private int estimatedTime;

//    private Integer wageAmmount;
//    private Integer clientChargeAmmount;

    private int actualTime;
    private double actuallWageAmmount;
    private double actualClientChargeAmmount;

    private double plannedWageAmount;
    private double plannedClientChargeAmount;
    private Date changedDateWageRate;
    private int objectID;

    public BudgetItem() {

    }

    /*
        public double getTotalWageAmmount() {
            if (actualTime == 0) {
                return ((double) estimatedTime / 60) * wageRate;
            } else {
                return ((double) getReminingTime() / 60) * wageRate + actuallWageAmmount;
            }
        }

        public double getTotalClientChargeAmmount() {
            if (actualTime == 0) {
                return ((double) estimatedTime / 60) * clientChargeRate;
            } else {
                return ((double) getReminingTime() / 60) * clientChargeRate + actualClientChargeAmmount;
            }
        }
    */
    public int getReminingTime() {
        if (estimatedTime < actualTime) {
            return 0;
        }
        return estimatedTime - actualTime;
    }

//    public Integer getAssigneeID() {
//        return assigneeID;
//    }
//
//    public void setAssigneeID(Integer assigneeID) {
//        this.assigneeID = assigneeID;
//    }


    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }


    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public double getWageRate() {
        return wageRate;
    }

    public void setWageRate(double wageRate) {
        this.wageRate = wageRate;
    }

    public double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public int getActualTime() {
        return actualTime;
    }

    public void setActualTime(int actualTime) {
        this.actualTime = actualTime;
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

    public double getActuallWageAmmount() {
        return actuallWageAmmount;
    }

    public void setActuallWageAmmount(double actuallWageAmmount) {
        this.actuallWageAmmount = actuallWageAmmount;
    }

    public double getActualClientChargeAmmount() {
        return actualClientChargeAmmount;
    }

    public void setActualClientChargeAmmount(double actualClientChargeAmmount) {
        this.actualClientChargeAmmount = actualClientChargeAmmount;
    }

    public Date getChangedDateWageRate() {
        return changedDateWageRate;
    }

    public void setChangedDateWageRate(Date changedDateWageRate) {
        this.changedDateWageRate = changedDateWageRate;
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

}
