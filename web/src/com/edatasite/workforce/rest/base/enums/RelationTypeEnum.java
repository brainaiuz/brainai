package com.edatasite.workforce.rest.base.enums;


import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 3/31/15.
 */
public enum RelationTypeEnum implements IsSerializable {

    PROJECT("PROJECT", "Project", RelationItem.TYPE_PROJECT),
    CONTRACT("CONTRACT", "Contract", RelationItem.TYPE_CONTRACT),
    TASK("CONTRACT", "Task", RelationItem.TYPE_TASK),
    ISSUE("ISSUE", "Issue", RelationItem.TYPE_ISSUE),
    PM_ISSUE("PM_ISSUE", "PM Issue", RelationItem.PM_ISSUE),
    EVENT("EVENT", "Event", RelationItem.TYPE_EVENT),
    CONTACT("CONTACT", "Contact", RelationItem.TYPE_CONTACT),
    LEAD("LEAD", "Lead", RelationItem.TYPE_LEAD),
    ACCOUNT("ACCOUNT", "Account", RelationItem.TYPE_CRM_ACCOUNT),
    OPPORTUNITY("OPPORTUNITY", "Opportunity", RelationItem.TYPE_OPPORTUNITY),
    CASE("CASE", "Case", RelationItem.TYPE_CASE),
    EMAIL_TRACKER("EMAIL_TRACKER", "Email Tracker", RelationItem.TYPE_EMAIL_TRACKER),
    MEETING_MINUTES("MEETING_MINUTES", "Meeting Minutes", RelationItem.TYPE_MEETING_MINUTES),
    BOOKING("BOOKING", "Booking", RelationItem.TYPE_BOOKING),
    SALE_QUOTE("SALE_QUOTE", "Sale Quote", RelationItem.TYPE_SALEQUOTE),
    SALES_INVOICE("SALES_INVOICE", "Sale Invoice", RelationItem.TYPE_SALEINVOICE),
    PRODUCT("PRODUCT", "Product", RelationItem.TYPE_PRODUCT),
    CANDIDATE("CANDIDATE", "Candidate", RelationItem.TYPE_CANDIDATE),
    EMPLOYEE("EMPLOYEE", "Employee", RelationItem.TYPE_EMPLOYEE),
    DEPARTMENT("DEPARTMENT", "Department", RelationItem.TYPE_DEPARTMENT),
    CLIENT("CLIENT", "Client", RelationItem.TYPE_CLIENT),
    SUPPLIER("SUPPLIER", "Supplier", RelationItem.TYPE_SUPPLIER),
    PURCHASE_ORDER("PURCHASE_ORDER", "Purchase Order", RelationItem.TYPE_PURCHASE_ORDER),
    CAMPAIGN("CAMPAIGN", "Campaign", RelationItem.TYPE_CAMPAIGN),
    LEAVE_REQUEST("LEAVE_REQUEST", "Leave Request", RelationItem.TYPE_LEAVE_REQUEST),
    EXPENSE_CLAIM("EXPENSE_CLAIM", "Expense Claim", RelationItem.TYPE_EXPENSE_CLAIM);


    private String code;
    private String name;
    private String key;

    RelationTypeEnum(String code, String name, String key) {
        this.code = code;
        this.name = name;
        this.key = key;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static String getRelationType(String type) {
        if (type == null) {
            return null;
        }
        return RelationTypeEnum.valueOf(type).getKey();

    }


}
