package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * User: Sherzod
 * Date: Aug 27, 2009
 * Time: 4:17:03 AM
 */
public interface PayrollConstants {
    WfmStrings wfmStrings2  = WfmStrings.App.get();

    int PENSION_AGE = 0;
    int COOPS = 1;
    int ANOTHER_JOB = 2;
    int COSR = 3;
    int MARRIED_WIDOW = 4;
    int ANOTHER_JOB2 = 5;
    int MARRIED_WIDOW2 = 6;
    int COMP = 7;
    int ANOTHER_JOB3 = 8;
    int MARRIED_WIDOW3 = 9;

    //salary amount constants
    String GROSS = "GROSS";
    String NET = "NET";
    //pay method constants
    String BACS = "BACS";
    String CASH = "CASH";
    String CHEQUE = "CHEQUE";
    String TRANSFER = "TRANSFER";
    //family status constants
    String SINGLE = "SINGLE";
    String MARRIED = "MARRIED";
    //form constants
    String P45 = "P45";
    String P46 = "P46";
    //letter description constants
    String LETTER_A = "A";
    String LETTER_B = "B";
    String LETTER_C = "C";
    String LETTER_D = "D";
    String LETTER_E = "E";
    String LETTER_F = "F";
    String LETTER_G = "G";
    String LETTER_J = "J";
    String LETTER_L = "L";
    String LETTER_S = "S";
    String LETTER_X = "X";

    String PAYMENT_SETTINGS = "PAYMENT_SETTINGS";
    String CATEGORY_PAYMENT = "Payment";
    String CATEGORY_DEDUCTION = "Deduction";
    String CATEGORY_TAX = "Tax";
    String CATEGORY_EMPLOYER_CONTRIBUTION = "EmployerContribution";
    String CATEGORY_LOAN = "Loan";

    String EMPTY_VALUE = "__nullOrEmptyValueInTheField__";

    int CHANGE_TYPE_PAYSLIP_DATE = 0;
    int CHANGE_TYPE_PAYSLIP_PERIOD = 1;

    int LINKED_TYPE_FIXED = 0;
    int LINKED_TYPE_PERCENTAGE_OF_BASIC = 1;
    Integer LINKED_TYPE_PERCENTAGE_OF_BASIC_AND_ALLOWANCE = 2;
    int LINKED_TYPE_MINIMUM_WAGE = 3;
    int LINKED_TYPE_PERCENTAGE_OF_BASIC_AND_ALLOWANCE_AFTER_TAX = 4;

    String CATEGORY_MATERIAL_AID = "MaterialAid";

    String MATERIAL_AID_TYPE_FUNERAL = "MaterialAidTypeFuneral";
    String MATERIAL_AID_TYPE_FAMILY_AFFAIRS = "MaterialAidTypeFamilyAffair";
    String MATERIAL_AID_TYPE_GIFT = "MaterialAidTypeGift";

    SelectItem[] SALARY_AMOUNT_LIST = new SelectItem[]{
            new SelectItem(0, "Gross", GROSS),
            new SelectItem(1, "Net", NET)
    };

    SelectItem[] PAY_METHOD_LIST = new SelectItem[]{
            new SelectItem(0, "BACS", BACS),
            new SelectItem(1, "Cash", CASH),
            new SelectItem(2, "Cheque", CHEQUE),
            new SelectItem(3, "Transfer", TRANSFER)
    };

    SelectItem[] PAY_METHOD_LIST_FOR_ARABIC = new SelectItem[]{
            new SelectItem(0, "Bank Transfer", TRANSFER),
            new SelectItem(2, "Cheque", CHEQUE),
            new SelectItem(3, "Cash", CASH)
    };

    SelectItem[] FAMILY_STATUS_LIST = new SelectItem[]{
            new SelectItem(0, wfmStrings2.familySingle(), SINGLE),
            new SelectItem(1, wfmStrings2.married(), MARRIED)};

    SelectItem[] FORMS_LIST = new SelectItem[]{
            new SelectItem(0, "P45", P45),
            new SelectItem(1, "P46", P46)};

    SelectItem[] TABLE_LETTER_LIST = new SelectItem[]{
            new SelectItem(0, "A", LETTER_A),
            new SelectItem(1, "B", LETTER_B),
            new SelectItem(2, "C", LETTER_C),
            new SelectItem(3, "D", LETTER_D),
            new SelectItem(4, "E", LETTER_E),
            new SelectItem(5, "F", LETTER_F),
            new SelectItem(6, "G", LETTER_G),
            new SelectItem(7, "J", LETTER_J),
            new SelectItem(8, "L", LETTER_L),
            new SelectItem(9, "S", LETTER_S),
            new SelectItem(10, "X", LETTER_S),
    };

}