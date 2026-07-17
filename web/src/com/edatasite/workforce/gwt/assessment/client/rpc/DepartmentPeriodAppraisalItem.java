package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Sher
 * Date: 8/16/12
 * Time: 2:15 PM
 */
public class DepartmentPeriodAppraisalItem implements IsSerializable {

    //period appraisal - sent for approval, approved, rejected
    public static final String _PERIOD_APPRAISAL = "_PERIOD_APPRAISAL";
    public static final String PERIOD_SENT_FOR_APPROVAL = "PERIOD_SENT_FOR_APPROVAL";
    public static final String PERIOD_APPROVED = "PERIOD_APPROVED";
    public static final String PERIOD_REJECTED = "PERIOD_REJECTED";

    public static final String ACTION = "action";
    public static final String VALIDITY_PERIOD = "validityPriod";
    public static final String DEPARTMENT_LEADER = "departmentLeader";
    public static final String DEPARTMENT_NAME = "departmentName";
    public static final String DATE = "date";
    public static final String STATUS = "status";
    public static final String COMMENTS = "COMMENTS";

    private Integer objectId;
    private Date date;
    private Integer departmentId;
    private Integer departmentLeaderId;
    private String departmentLeaderName;
    private String departmentName;
    private ValidityPeriodItem validityPeriodItem;
    private BonusSettingsItem bonusSettingsItem;
    private Integer employeeAssessed;
    private Integer employeeNotAssessed;
    private String comment;
    private String statusCode;
    private String statusName;
    private HashMap<String, Integer> scoreMap;
    private Integer membersCount;
    private ArrayList<Integer> employeeAssessments = new ArrayList<>();
    private ArrayList<AssessmentsListElem> assessmentsListElems = new ArrayList<>();

    private String rejectionReasonComment;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDepartmentLeaderName() {
        return departmentLeaderName;
    }

    public void setDepartmentLeaderName(String departmentLeaderName) {
        this.departmentLeaderName = departmentLeaderName;
    }

    public ValidityPeriodItem getValidityPeriodItem() {
        return validityPeriodItem;
    }

    public void setValidityPeriodItem(ValidityPeriodItem validityPeriodItem) {
        this.validityPeriodItem = validityPeriodItem;
    }

    public Integer getEmployeeAssessed() {
        return employeeAssessed;
    }

    public void setEmployeeAssessed(Integer employeeAssessed) {
        this.employeeAssessed = employeeAssessed;
    }

    public Integer getEmployeeNotAssessed() {
        return employeeNotAssessed;
    }

    public void setEmployeeNotAssessed(Integer employeeNotAssessed) {
        this.employeeNotAssessed = employeeNotAssessed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public BonusSettingsItem getBonusSettingsItem() {
        return bonusSettingsItem;
    }

    public void setBonusSettingsItem(BonusSettingsItem bonusSettingsItem) {
        this.bonusSettingsItem = bonusSettingsItem;
    }

    public HashMap<String, Integer> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(HashMap<String, Integer> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    public ArrayList<Integer> getEmployeeAssessments() {
        return employeeAssessments;
    }

    public void setEmployeeAssessments(ArrayList<Integer> employeeAssessments) {
        this.employeeAssessments = employeeAssessments;
    }

    public ArrayList<AssessmentsListElem> getAssessmentsListElems() {
        return assessmentsListElems;
    }

    public void setAssessmentsListElems(ArrayList<AssessmentsListElem> assessmentsListElems) {
        this.assessmentsListElems = assessmentsListElems;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDepartmentLeaderId() {
        return departmentLeaderId;
    }

    public void setDepartmentLeaderId(Integer departmentLeaderId) {
        this.departmentLeaderId = departmentLeaderId;
    }

    public String getRejectionReasonComment() {
        return rejectionReasonComment;
    }

    public void setRejectionReasonComment(String rejectionReasonComment) {
        this.rejectionReasonComment = rejectionReasonComment;
    }
}