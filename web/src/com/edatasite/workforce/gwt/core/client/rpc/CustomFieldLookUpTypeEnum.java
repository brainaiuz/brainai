package com.edatasite.workforce.gwt.core.client.rpc;

/**
 * User: Abror Abdukadirov
 * Date: 23.07.2019 17:48
 */
public enum CustomFieldLookUpTypeEnum {
    BUSINESS_GOAL,
    CANDIDATE,
    CASE,
    COMPANY_GOAL,
    CONTACT,
    CONTRACT,
    COUNTRY,
    CURRENCY,
    CUSTOMER,
    DEPARTMENT,
    DEPARTMENT_GOAL,
    EMPLOYEE,
    USER,
    LEAD,
    LOCATION,
    OPPORTUNITY,
    OPPORTUNITY_NAME,
    PAYMENT_METHOD,
    PERSONAL_GOAL,
    POSITION,
    PRODUCT,
    PRODUCT_CATEGORY,
    PROJECT,
    PROJECT_GOAL,
    PURCHASE_INVOICE,
    PURCHASE_ORDER,
    REFERENCE,
    SALES_INVOICE,
    SALES_QUOTE,
    SUPPLIER,
    TASK,
    TERMS,
    TIMESLOT,
    UNIT_MEASUREMENT,
    VACANCY,
    SALES_ORDER,
    CUSTOM_FORM;

    public static CustomFieldLookUpTypeEnum get(String type) {
        if (type == null) {
            return null;
        }
        for (CustomFieldLookUpTypeEnum value : CustomFieldLookUpTypeEnum.values()) {
            if (type.equals(value.name())) {
                return value;
            }
        }
        return null;
    }
}
