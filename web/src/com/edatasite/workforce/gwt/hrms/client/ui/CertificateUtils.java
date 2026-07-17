package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.hrms.client.EmployeeProfileConstans;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Khasan on 30.09.14.
 */
public class CertificateUtils implements EmployeeProfileConstans {

    //Employment
    public static Map<String, String> getEmploymentFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EMPLOYEE_CODE, "employee code");
        values.put(CERTIFICATE_LETTERS, "certificate letters");
        values.put(CERTIFICATE_NUMBERS, "certificate numbers");
        values.put(SALUTATION, "salutation");
        values.put(FIRST_NAME, "first name");
        values.put(LAST_NAME, "last name");
        values.put(MIDDLE_NAME, "middlename");
        values.put(DATE_OF_BIRTH, "dateofbirth");
        values.put(NATIONALITY, "nationality");
        values.put(MARITAL_STATUS, "marital status");
        values.put(EMAIL, "email");
        values.put(PHONE_NUMBER, "phone number");
        values.put(PASSPORT_NUMBER, "passport number");
        values.put(PASSPORT_ISSUED_BY, "passport issued by");
        values.put(DEPARTMENT, "department");
        values.put(POSITION, "position");
        values.put(CURRENCY, "currency");
        values.put(WAGE_RATE, "wagerate");
        values.put(CLIENT_CHARGE_RATE, "chargerate");
        values.put(SALARY_AMOUNT, "salary amount");
        values.put(HIRE_DATE, "hire date");
        values.put(FIRE_DATE, "fire date");
        values.put(JOB_TITLE, "job title");
        values.put(BANK_NAME, "bank name");
        values.put(BANK_ADDRESS, "bank address");
        values.put(ACCOUNT_NUMBER, "account number");
        values.put(ACCOUNT_NAME, "account name");
        values.put(SWIFT_CODE, "swift code");
        values.put(SORT_CODE, "sort code");
        values.put(IBAN_CODE, "iban code");
//        values.put(YOUR_NAME, "your name");
        values.put(YOUR_ROLE, "your role");
        values.put(COMPANY_NAME, "company name");
        values.put(COMPANY_ADDRESS, "company address");
        values.put(CURRENT_DATE, "current date");
//        values.put(GENERAL_ALLOWANCE, "general allowance");
        values.put(SALARY_IN_WORD, "salary in word");
        values.put(TIMESLOT, "timeslot");
        values.put(HIS_HER, "His or Her");
        values.put(HIS_HER_LOWER_CASE, "his or her");
        values.put(HE_SHE, "He or She");
        values.put(HE_SHE_LOWER_CASE, "he or she");
        values.put(HIM_HER, "Him or Her");
        values.put(HIM_HER_LOWER_CASE, "him or her");
        values.put(ISSUER_NAME, "issuer name");
        values.put(ISSUER_POSITION, "issuer position");
        values.put(BASIC_ALLOWANCE, "basic allowance");
        values.put(TOTAL_ALLOWANCES, "total allowances");
        values.put(TOTAL_DEDUCTIONS, "total deductions");
        values.put(TOTAL_LOANS, "total loans");
        values.put(GROSS_SALARY, "gross salary");
        values.put(GROSS_SALARY_INWORDS, "gross salary in words");
        values.put(BASIC_ALLOWANCE_AMOUNT, "basic allowance amount");
        values.put(SUPERVISOR, "supervisor");
        values.put(VISA_NUMBER, "visa number");
        values.put(VISA_EXPIRATION_DATE, "visa expiry date");
        values.put(LOCATION, "location");
        values.put(LAST_LEAVE_REQUEST_START_DATE, "last leave request start date");
        values.put(LAST_LEAVE_REQUEST_END_DATE, "last leave request end date");
        values.put(EMPLOYEE_ADDRESS, "employee address");
        values.put(HOUSING_ALLOWANCE_IN_WORDS, "housing allowance in words");
        values.put(HOUSING_ALLOWANCE, "housing allowance");
        values.put(TRANSPORT_ALLOWANCE, "transport allowance");
        values.put(TRANSPORT_ALLOWANCE_IN_WORDS, "transport allowance in words");
        values.put(ACCOMODATION_ALLOWANCE, "accomodation allowance");
        values.put(ACCOMODATION_ALLOWANCE_IN_WORDS, "accomodation allowance in words");
        values.put(AIR_TICKET_REIMBURSEMENT, "air ticket reimbursement");
        values.put(AIR_TICKET_REIMBURSEMENT_IN_WORDS, "air ticket reimbursement in words");
        values.put(AIR_TICKET_ALLOWANCE, "air ticket allowance");
        values.put(AIR_TICKET_ALLOWANCE_IN_WORDS, "air ticket allowance in words");
        values.put(ARREARS, "arrears");
        values.put(ARREARS_IN_WORDS, "arrears in words");
        values.put(BASIC_SALARY, "basic salary");
        values.put(BASIC_SALARY_IN_WORDS, "basic salary in words");
        values.put(BENEFIT_PAYMENT, "benefit payment");
        values.put(BENEFIT_PAYMENT_IN_WORDS, "benefit payment in words");
        values.put(COST_OF_LIVING_ALLOWANCE, "cost of living allowance");
        values.put(COST_OF_LIVING_ALLOWANCE_IN_WORDS, "cost of living allowance in words");
        values.put(EDUCATION_ALLOWANCE, "education allowance");
        values.put(EDUCATION_ALLOWANCE_IN_WORDS, "education allowance in words");
        values.put(END_OF_SERVICE_GRATUITY, "end of service");
        values.put(END_OF_SERVICE_GRATUITY_IN_WORDS, "end of service in words");
        values.put(EXPENSE_REPORT, "expense report");
        values.put(EXPENSE_REPORT_IN_WORDS, "expense report in words");
        values.put(EXTRA_ADDITIONAL, "extra additional");
        values.put(EXTRA_ADDITIONAL_IN_WORDS, "extra additional in words");
        values.put(FAMILY_ALLOWANCE, "family allowance");
        values.put(FAMILY_ALLOWANCE_IN_WORDS, "family allowance in words");
        values.put(FAR_LOCATION_ALLOWANCE, "far location allowance");
        values.put(FAR_LOCATION_ALLOWANCE_IN_WORDS, "far location allowance in words");
        values.put(JOB_ALLOWANCE, "job allowance");
        values.put(JOB_ALLOWANCE_IN_WORDS, "job allowance in words");
        values.put(LEAVE_ENCASHMENT, "leave encashment");
        values.put(BONUS, "bonus");
        values.put(BONUS_IN_WORDS, "bonus in words");
        values.put(LEAVE_ENCASHMENT_IN_WORDS, "leave encashment in words");


//        values.put()
        return values;
    }

    public static SelectItem[] getCertificateFields() {
        return new SelectItem[]{
                new SelectItem(1, BOX_1),
                new SelectItem(2, BOX_2),
                new SelectItem(3, BOX_3),
                new SelectItem(4, BOX_4),
                new SelectItem(5, BOX_5),
                new SelectItem(6, BOX_6),
                new SelectItem(7, BOX_7),
                new SelectItem(8, BOX_8),
                new SelectItem(9, BOX_9),
                new SelectItem(10, BOX_10),
                new SelectItem(11, BOX_11),
                new SelectItem(12, BOX_12),
                new SelectItem(13, BOX_13),
                new SelectItem(14, BOX_14),
                new SelectItem(15, BOX_15),
                new SelectItem(16, BOX_16),
                new SelectItem(17, BOX_17),
                new SelectItem(18, BOX_18),
                new SelectItem(19, TEXT_AREA_1),
                new SelectItem(20, TEXT_AREA_2),
                new SelectItem(21, TEXT_AREA_3),
                new SelectItem(22, TEXT_AREA_4),
                new SelectItem(23, TEXT_AREA_5),
                new SelectItem(24, TEXT_AREA_6),
                new SelectItem(25, TEXT_AREA_7),
                new SelectItem(26, TEXT_AREA_8)};
    }


}
