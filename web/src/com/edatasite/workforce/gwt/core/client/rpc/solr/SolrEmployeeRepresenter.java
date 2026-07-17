package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Djuraev on 10/30/15.
 */
public class SolrEmployeeRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_LOOKUP_COMPOSITE = "lookupComposite";
    public static final String FIELD_LOOKUP_COMPOSITE_MOBILE = "lookupCompositeMobile";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_EMPLOYEE_ID = "employeeId";
    public static final String FIELD_EMPLOYEE_NUMBER = "employeeNumber";
    public static final String FIELD_EMPLOYEE_INTEGER_NUMBER = "employeeIntegerNumber";
    public static final String FIELD_EMPLOYEE_NAME = "employeeName";
    public static final String FIELD_EMPLOYEE_FIRST_NAME = "firstName";
    public static final String FIELD_EMPLOYEE_LAST_NAME = "lastName";
    public static final String FIELD_PHONE_NUMBER = "phoneNumber";
    public static final String FIELD_EMAIL = "email";

    public static final String FIELD_COUNTRY_ID = "countryId";
    public static final String FIELD_COUNTRY_NAME = "countryName";
    public static final String FIELD_COUNTRY_CODE = "countryCode";
    public static final String FIELD_COUNTRY_ID_CODE = "countryIdCode";
    public static final String FIELD_COUNTRY_ID_CODE_NAME = "countryIdCodeName";
    public static final String FIELD_STATE_ID = "stateId";
    public static final String FIELD_STATE_NAME = "stateName";
    public static final String FIELD_STATE_ID_NAME = "stateIdName";
    public static final String FIELD_STREET = "street";
    public static final String FIELD_STREET2 = "street2";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_POST_CODE = "postCode";

    public static final String FIELD_POSITION_ID = "positionId";
    public static final String FIELD_POSITION_NAME = "positionName";
    public static final String FIELD_POSITION_ID_NAME = "positionIdName";
    public static final String FIELD_ROLE_ID = "roleId";
    public static final String FIELD_ROLE_NAME = "roleName";
    public static final String FIELD_ROLE_CODE = "roleCode";
    public static final String FIELD_ROLE_ID_NAME = "roleIdName";
    public static final String FIELD_LANGUAGE_ID = "languageId";
    public static final String FIELD_LANGUAGE_NAME = "languageName";
    public static final String FIELD_LANGUAGE_CODE = "languageCode";
    public static final String FIELD_LANGUAGE_ID_NAME = "languageIdName";
    public static final String FIELD_SKILL_ID = "skillId";
    public static final String FIELD_SKILL_NAME = "skillName";
    public static final String FIELD_SKILL_ID_NAME = "skillIdName";
    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME = "statusName";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_STATUS_ID_NAME = "statusIdName";
    public static final String FIELD_LOCATION_ID = "locationId";
    public static final String FIELD_LOCATION_NAME = "locationName";
    public static final String FIELD_LOCATION_ID_NAME = "locationIdName";
    public static final String FIELD_LOCATION_STATE = "locationState";
    public static final String FIELD_LOCATION_CITY = "locationCity";
    public static final String FIELD_DEPARTMENT_ID = "departmentId";
    public static final String FIELD_DEPARTMENT_NAME = "departmentName";
    public static final String FIELD_DEPARTMENT_ID_NAME = "departmentIdName";
    public static final String FIELD_DRIVER_ID = "driverId";
    public static final String FIELD_PASSPORT_NUMBER = "passportNumber";
    public static final String FIELD_PASSPORT_ISSUED_BY = "passportIssuedBy";
    public static final String FIELD_PASSPORT_ISSUED_ID = "passportIssuedId";
    public static final String FIELD_INSURANCE_NUMBER = "insuranceNumber";
    public static final String FIELD_VISA_NUMBER = "visaNumber";
    public static final String FIELD_AGENT_NAME = "agentName";
    public static final String FIELD_BANK_NAME = "bankName";
    public static final String FIELD_ACCOUNT_NUMBER = "accountNumber";
    public static final String FIELD_ACCOUNT_NAME = "accountName";
    public static final String FIELD_BANK_ADDRESS = "bankAddress";
    public static final String FIELD_SWIFT_CODE = "swiftCode";
    public static final String FIELD_SORT_CODE = "sortCode";
    public static final String FIELD_IBAN_CODE = "ibanCode";

    public static final String FIELD_SUPERVISOR_ID = "supervisorId";
    public static final String FIELD_SUPERVISOR_NAME = "supervisorName";
    public static final String FIELD_SUPERVISOR_ID_NAME = "supervisorIdName";

    public static final String FIELD_WAGE_RATE = "wageRate";
    public static final String FIELD_CLIENT_CHARGE_RATE = "clientChargeRate";

    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdate";
    public static final String FIELD_BIRTH_DATE = "birthDate";
    public static final String FIELD_HIRE_DATE = "hireDate";
    public static final String FIELD_END_DATE = "endDate";
    public static final String FIELD_INSURANCE_EXPIRY_DATE = "insuranceExpiryDate";
    public static final String FIELD_PASSPORT_ISSUE_DATE = "passportIssueDate";
    public static final String FIELD_PASSPORT_EXPIRE_DATE = "passportExpireDate";
    public static final String FIELD_VISA_ISSUE_DATE = "visaIssueDate";
    public static final String FIELD_VISA_EXPIRE_DATE = "visaExpireDate";
    public static final String FIELD_GENDER_NAME = "genderName";
    public static final String FIELD_CURRENCY_ID = "currencyId";
    public static final String FIELD_CURRENCY_NAME = "currencyName";
    public static final String FIELD_CURRENCY_ID_NAME = "currencyIdName";
    public static final String FIELD_PAYROLL_BATCH_ID = "payrollBatchId";

    public static final String FIELD_OPENING_BALANCE_DAYS = "openingBalanceDays";
    public static final String FIELD_PROBATION_DAYS = "probationDays";

    public static final String SORTABLE_EMPLOYEE_NUMBER = "sortableNumber";
    public static final String SORTABLE_EMPLOYEE_NAME = "sortableEmployeeName";
    public static final String SORTABLE_FIRST_NAME = "sortableFirstName";
    public static final String SORTABLE_LAST_NAME = "sortableLastName";
    public static final String SORTABLE_PHONE_NUMBER = "sortablePhoneNumber";
    public static final String SORTABLE_EMAIL = "sortableEmail";
    public static final String SORTABLE_POSITION_NAME = "sortablePositionName";
    public static final String SORTABLE_ROLE_NAME = "sortableRoleName";
    public static final String SORTABLE_STATUS_NAME = "sortableStatusName";
    public static final String SORTABLE_DEPARTMENT_NAME = "sortableDepartmentName";
    public static final String SORTABLE_PASSPORT_NUMBER = "sortablePassportNumber";
    public static final String SORTABLE_PASSPORT_ISSUED_BY = "sortablePassportIssuedBy";
    public static final String SORTABLE_INSURANCE_NUMBER = "sortableInsuranceNumber";
    public static final String SORTABLE_VISA_NUMBER = "sortableVisaNumber";
    public static final String SORTABLE_SUPERVISOR_NAME = "sortableSupervisorName";
    public static final String SORTABLE_GENDER_NAME = "sortableGenderName";
    public static final String SORTABLE_COUNRTY_NAME = "sortableCountryName";
    public static final String SORTABLE_LOCATION_NAME = "sortableLocationName";
    public static final String SORTABLE_STREET = "sortableStreet";
    public static final String SORTABLE_STREET2 = "sortableStreet2";
    public static final String SORTABLE_CITY = "sortableCity";
    public static final String SORTABLE_STATE_NAME = "sortableStateName";
    public static final String SORTABLE_CURRENCY_NAME = "sortableCurrencyName";

    public static final String FIELD_DYN_STRING_COMPOSITE = "dynStringComposite";

    public static final String FIELD_EMPLOYEE_MIDDLE_NAME = "middleName";
    public static final String FIELD_ROLE_NAME_ALL = "roleAll";
    public static final String FIELD_STATUS_ID_CODE = "statusIdCode";

    public static final String FIELD_POSITION_NAME_UZ = "positionNameUz";
    public static final String FIELD_POSITION_NAME_RU = "positionNameRu";
    public static final String FIELD_POSITION_NAME_EN = "positionNameEn";
    public static final String FIELD_POSITION_NAME_AR = "positionNameAr";

    public static final String FIELD_LOCATION_NAME_UZ = "locationNameUz";
    public static final String FIELD_LOCATION_NAME_RU = "locationNameRu";
    public static final String FIELD_LOCATION_NAME_EN = "locationNameEn";
    public static final String FIELD_LOCATION_NAME_AR = "locationNameAr";
    public static final String FIELD_SALARY_AMOUNT = "salaryAmount";
    public static final String FIELD_MARTIAL_STATUS_ID = "martialStatusId";
    public static final String FIELD_DEPARTMENT_NAME_UZ = "departmentNameUz";
    public static final String FIELD_DEPARTMENT_NAME_RU = "departmentNameRu";
    public static final String FIELD_DEPARTMENT_NAME_EN = "departmentNameEn";
    public static final String FIELD_DEPARTMENT_NAME_AR = "departmentNameAr";

    public static final String FIELD_QUALIFICATION_ID = "qualificationId";
    public static final String FIELD_QUALIFICATION_NAME = "qualificationName";
    public static final String FIELD_QUALIFICATION_ID_NAME = "qualificationIdName";

    public static final String SORTABLE_POSITION_NAME_UZ = "sortablePositionNameUz";
    public static final String SORTABLE_POSITION_NAME_RU = "sortablePositionNameRu";
    public static final String SORTABLE_POSITION_NAME_AR = "sortablePositionNameAr";
    public static final String SORTABLE_POSITION_NAME_EN = "sortablePositionNameEn";

    public static final String SORTABLE_DEPARTMENT_NAME_UZ = "sortableDepartmentUz";
    public static final String SORTABLE_DEPARTMENT_NAME_RU = "sortableDepartmentRu";
    public static final String SORTABLE_DEPARTMENT_NAME_EN = "sortableDepartmentEn";
    public static final String SORTABLE_DEPARTMENT_NAME_AR = "sortableDepartmentAr";

    public static final String SORTABLE_QUALIFICATION_NAME = "sortablequalificationName";
    public static final String FIELD_CONTACT_IN_NAME = "contactIdName";
    public static final String FIELD_CONTACT_ID = "contactId";
    public static final String FIELD_CONTACT_NAME = "contactName";
    public static final String FIELD_TIMESLOT_ID_NAME = "timeslotIdName";
    public static final String FIELD_TIMESLOT_ID = "timeslotId";
    public static final String FIELD_TIMESLOT_NAME = "timeslotName";

    public static final String FIELD_POSITION_TYPE_ID = "positionTypeId";
    public static final String FIELD_POSITION_TYPE_NAME = "positionTypeName";
    public static final String FIELD_POSITION_TYPE_NAME_UZ = "positionTypeNameUz";
    public static final String FIELD_POSITION_TYPE_NAME_RU = "positionTypeNameRu";
    public static final String FIELD_POSITION_TYPE_NAME_EN = "positionTypeNameEn";
    public static final String FIELD_POSITION_TYPE_NAME_AR = "positionTypeNameAr";
    public static final String FIELD_POSITION_TYPE_ID_NAME = "positionTypeIdName";

}
