package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class AssessmentSkills implements IsSerializable, Constants {

    private AssessmentRatingsComments[] ratingsComments;
    private String status;
    private String team;
    private String employeeName;
    private Integer employeeId;
	private Integer managerID;
	private boolean currentUserManager = false;
    private String managerName;
    private boolean turn;
    private Integer keyEmployeeAssessmentId;
    private double calculatedAverage;
    private Date initiateDate;

    public Integer getKeyEmployeeAssessmentId() {
        return keyEmployeeAssessmentId;
    }

    public void setKeyEmployeeAssessmentId(Integer keyEmployeeAssessmentId) {
        this.keyEmployeeAssessmentId = keyEmployeeAssessmentId;
    }

    public AssessmentRatingsComments[] getRatingsComments() {
        return ratingsComments;
    }

    public void setRatingsComments(AssessmentRatingsComments[] ratingsComments) {
        this.ratingsComments = ratingsComments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

	public Integer getManagerID() {
		return managerID;
	}

	public void setManagerID(Integer managerID) {
		this.managerID = managerID;
	}

    public boolean isCurrentUserManager() {
        return currentUserManager;
    }

    public void setCurrentUserManager(boolean is) {
        this.currentUserManager = is;
    }

	public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public double getCalculatedAverage() {
        return calculatedAverage;
    }

    public void setCalculatedAverage(double calculatedAverage) {
        this.calculatedAverage = calculatedAverage;
    }

    public Date getInitiateDate() {
        return initiateDate;
    }

    public void setInitiateDate(Date initiateDate) {
        this.initiateDate = initiateDate;
    }
}
