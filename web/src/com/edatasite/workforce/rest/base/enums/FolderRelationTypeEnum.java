package com.edatasite.workforce.rest.base.enums;


import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 3/31/15.
 */
public enum FolderRelationTypeEnum implements IsSerializable {

    PROJECT(Constants.F_PROJECT, "PROJECT", "Project"),
    PROJECTS(Constants.F_PROJECT, "PROJECTS", "Project"),
    TASK(Constants.F_TASK, "TASK", "Task"),
    TASKS(Constants.F_TASK, "TASKS", "Task"),
    CONTACT(Constants.F_CRM_CONTACT, "CONTACT", "Contact"),
    CONTACTS(Constants.F_CRM_CONTACT, "CONTACTS", "Contact"),
    ISSUE(Constants.F_PR_ISSUE, "ISSUE", "Issue"),
    LEAD(Constants.F_LEAD, "LEAD", "Lead"),
    LEADS(Constants.F_LEAD, "LEADS", "Lead"),
    ACCOUNT(Constants.F_CRM_ACCOUNT, "ACCOUNT", "Account"),
    COMPANIES(Constants.F_CRM_ACCOUNT, "COMPANIES", "Account"),
    LEAVE_REQUEST(Constants.F_LEAVE_REQUEST, "LEAVE_REQUEST", "Leave Request"),
    LEAVE_REQUESTS(Constants.F_LEAVE_REQUEST, "LEAVE_REQUESTS", "Leave Request"),
    NOTE(Constants.F_NOTE, "NOTE", "Note"),
    NOTES(Constants.F_NOTE, "NOTES", "Note"),
    EMPLOYEE(Constants.F_EMPLOYEE_PROFILE, "EMPLOYEE", "Employee"),
    EMPLOYEES(Constants.F_EMPLOYEE_PROFILE, "EMPLOYEES", "Employee"),
    OPPORTUNITY(Constants.F_OPPORTUNITY, "OPPORTUNITY", "Opportunity"),
    OPPORTUNITIES(Constants.F_OPPORTUNITY, "OPPORTUNITIES", "Opportunity"),
    EXPENSE(Constants.F_EXP_DOC, "EXPENSE", "Expense"),
    EXPENSES(Constants.F_EXP_DOC, "EXPENSES", "Expense"),
    EXPENSE_ITEM(Constants.F_EXP, "EXPENSE_ITEM", "Expense Item"),
    EVENT(Constants.F_EVENT, "EVENT", "Event"),
    ACTIVITIES(Constants.F_EVENT, "ACTIVITIES", "Activities"),
    CASES(Constants.F_CASE, "CASES", "Cases"),
    CANDIDATES(Constants.F_CANDIDATE, "CANDIDATES", "Candidates"),
    SALES_INVOICE(Constants.F_SALE_INV, "SALES_INVOICE", "Sales Invoice"),
    PRODUCT(Constants.F_PRODUCTS_SERVICES, "PRODUCTS_SERVICES", "Products&Services");

    private Integer id;
    private String name;
    private String code;

    FolderRelationTypeEnum(Integer id, String code, String name) {
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
        FolderRelationTypeEnum relationType = FolderRelationTypeEnum.valueOf(relationTypeStr.toUpperCase());
        return switch (relationType) {
            case PROJECT -> Constants.F_PROJECT;
            case TASK, TASKS -> Constants.F_TASK;
            case CONTACT, CONTACTS -> Constants.F_CRM_CONTACT;
            case ACCOUNT, COMPANIES -> Constants.F_CRM_ACCOUNT;
            case ISSUE -> Constants.F_PR_ISSUE;
            case LEAVE_REQUEST -> Constants.F_LEAVE_REQUEST;
            case LEAD, LEADS -> Constants.F_LEAD;
            case NOTE, NOTES -> Constants.F_NOTE;
            case EMPLOYEE -> Constants.F_EMPLOYEE_PROFILE;
            case OPPORTUNITY, OPPORTUNITIES -> Constants.F_OPPORTUNITY;
            case EXPENSE -> Constants.F_EXP_DOC;
            case EXPENSE_ITEM -> Constants.F_EXP;
            case EVENT, ACTIVITIES -> Constants.F_EVENT;
            case CASES -> Constants.F_CASE;
            case CANDIDATES -> Constants.F_CANDIDATE;
            case SALES_INVOICE -> Constants.F_SALE_INV;
            case PRODUCT -> Constants.F_PRODUCTS_SERVICES;
            default -> Constants.F_DEFAULT;
        };
    }


}
