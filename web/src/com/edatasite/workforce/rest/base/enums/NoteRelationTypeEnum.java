package com.edatasite.workforce.rest.base.enums;


import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 3/31/15.
 */
public enum NoteRelationTypeEnum implements IsSerializable {

    PROJECT(EdsNoteHistory.PROJECT, "PROJECT", "Project"),
    TASK(EdsNoteHistory.TASK, "TASK", "Task"),
    CLIENT(EdsNoteHistory.CLIENT, "CLIENT", "Client"),
    EMPLOYEE(EdsNoteHistory.EMPLOYEE, "EMPLOYEE", "Employee"),
    DEPARTMENT(EdsNoteHistory.DEPARTMENT, "DEPARTMENT", "Department"),
    SUPPLIER(EdsNoteHistory.SUPPLIER, "SUPPLIER", "Supplier"),
    ISSUE(EdsNoteHistory.PM_ISSUE, "ISSUE", "Issue"),
    MEETING_MINUTES(EdsNoteHistory.MEETING_MINUTES, "MEETING_MINUTES", "Meeting Minutes"),
    PERSONAL_GOAL(EdsNoteHistory.PERSONAL_GOAL, "PERSONAL_GOAL", "Personal Goal"),
    DEPARTMENT_GOAL(EdsNoteHistory.DEPARTMENT_GOAL, "DEPARTMENT_GOAL", "Department Goal"),
    PROJECT_GOAL(EdsNoteHistory.PROJECT_GOAL, "PROJECT_GOAL", "Project Goal"),
    BUSINESS_GOAL(EdsNoteHistory.BUSINESS_GOAL, "BUSINESS_GOAL", "Business Goal"),
    COMPANY_GOAL(EdsNoteHistory.COMPANY_GOAL, "COMPANY_GOAL", "Company Goal"),
    VACANCY(EdsNoteHistory.VACANCY, "VACANCY", "Vacancy"),
    PLACEMENT(EdsNoteHistory.PLACEMENT, "PLACEMENT", "Placement"),
    CONTACT(EdsNoteHistory.CRM_CONTACT, "CONTACT", "Contact"),
    CANDIDATE(EdsNoteHistory.CANDIDATE, "CANDIDATE", "Candidate"),
    LEAD(EdsNoteHistory.CRM_LEAD, "LEAD", "Lead"),
    ACCOUNT(EdsNoteHistory.CRM_ACCOUNT, "ACCOUNT", "Account"),
    CASE(EdsNoteHistory.CRM_CASE, "CASE", "Case"),
    OPPORTUNITY(EdsNoteHistory.CRM_OPPORTUNITY, "OPPORTUNITY", "Opportunity"),
    CAMPAIGN(EdsNoteHistory.CRM_CAMPAIGN, "CAMPAIGN", "Campaign"),
    CONTRACT(EdsNoteHistory.PM_CONTRACT, "CONTRACT", "Contract"),;

    private Integer id;
    private String name;
    private String code;

    NoteRelationTypeEnum(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Integer getRelationType(String relationTypeStr) {
        NoteRelationTypeEnum relationType = NoteRelationTypeEnum.valueOf(relationTypeStr.toUpperCase());
        return switch (relationType) {
            case PROJECT -> EdsNoteHistory.PROJECT;
            case TASK -> EdsNoteHistory.TASK;
            case CLIENT -> EdsNoteHistory.CLIENT;
            case EMPLOYEE -> EdsNoteHistory.EMPLOYEE;
            case DEPARTMENT -> EdsNoteHistory.DEPARTMENT;
            case SUPPLIER -> EdsNoteHistory.SUPPLIER;
            case ISSUE -> EdsNoteHistory.PM_ISSUE;
            case MEETING_MINUTES -> EdsNoteHistory.MEETING_MINUTES;
            case PERSONAL_GOAL -> EdsNoteHistory.PERSONAL_GOAL;
            case DEPARTMENT_GOAL -> EdsNoteHistory.DEPARTMENT_GOAL;
            case PROJECT_GOAL -> EdsNoteHistory.PROJECT_GOAL;
            case BUSINESS_GOAL -> EdsNoteHistory.BUSINESS_GOAL;
            case COMPANY_GOAL -> EdsNoteHistory.COMPANY_GOAL;
            case VACANCY -> EdsNoteHistory.VACANCY;
            case PLACEMENT -> EdsNoteHistory.PLACEMENT;
            case CONTACT -> EdsNoteHistory.CRM_CONTACT;
            case CANDIDATE -> EdsNoteHistory.PERSONAL_GOAL;
            case LEAD -> EdsNoteHistory.CRM_LEAD;
            case ACCOUNT -> EdsNoteHistory.CRM_ACCOUNT;
            case CASE -> EdsNoteHistory.CRM_CASE;
            case OPPORTUNITY -> EdsNoteHistory.CRM_OPPORTUNITY;
            case CAMPAIGN -> EdsNoteHistory.CRM_CAMPAIGN;
            case CONTRACT -> EdsNoteHistory.PM_CONTRACT;
        };
    }


}
