package com.edatasite.workforce.gwt.webforms.client;

import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;

/**
 * User: Hayot
 * Date: Aug 5, 2010
 * Time: 7:26:39 AM
 */
public interface WebFormConstants {
    String DELIMITR = "::";
    //field Types
    int INPUT_TEXTBOX = 1;
    int INPUT_TEXTBOX2 = 12;
    int INPUT_TEXTAREA = 7;
    int INPUT_TEXTAREA2 = 72;
    int INPUT_RADIO_BUTTON = 2;
    int INPUT_CHECKBOX = 3;
    int INPUT_DROPDOWN = 4;
    int INPUT_DATEPICKER = 5;
    int INPUT_PHONENUMBER = 6;
    int CRM_REPORTEDBY_CASE = 8;
    int INPUT_ATTACHMENT = 9;
    int INPUT_MAILING_LIST = 10;
    int INPUT_DOB = 11;
    int INPUT_VACANCIES = 13;


    String WEB_FORM = "_WEB_FORM"; // reference parent code... children : lead, account, case.
    String LEAD_FORM = "LEAD_FORM";//lead Form child of type reference; object : type
    String CRM_ACCOUNT_FORM = "CRM_ACCOUNT_FORM";//account Form child of type reference; object : type
    String CASE_FORM = "CASE_FORM";//case Form child of type reference; object : type
    String CANDIDATE_FORM = LayoutRPC.CANDIDATE_FORM;//candidate Form child of type reference; object : type
    String OPPORTUNITY_FORM = "Opportunity Form";//Opportunity Form child of type reference; object : type
    int LEAD_FORM_ = 1;//lead Form child of type reference; object : type
    int ACCOUNT_FORM_ = 2;//lead Form child of type reference; object : type
    int CASE_FORM_ = 3;//lead Form child of type reference; object : type
    int OPPORTUNITY_FORM_ = 4;//Opportunity Form child of type reference; object : type

    String CRM_LISTS = "_CRM_LISTS"; // reference parent code... children : leadList, accountList, caseList.
    String LEAD_LIST = "CRM_LEAD_LIST";//lead Form child of type reference; object : type
    String ACCOUNT_LIST = "CRM_ACCOUNT_LIST";//lead Form child of type reference; object : type
    String CASE_LIST = "CRM_CASE_LIST";//lead Form child of type reference; object : type

    String DROPDOWNITEMS_ASSIGNEES = "ASSIGNEES";
    String DROPDOWNITEMS_OWNERS = "OWNERS";
    String DROPDOWNITEMS_COUNTIRES = "COUNTIRIES";
    String DROPDOWNITEMS_STATES = "STATES";
    String DROPDOWNITEMS_SOURCES = "SOURCES";
    String DROPDOWNITEMS_CAMPAIGNS = "CAMPAIGNS";
    String DROPDOWNITEMS_STATUSES = "STATUSES";
    String DROPDOWNITEMS_INDUSTRIES = "INDUSTRIES";
    String DROPDOWNITEMS_RATINGS = "RATINGS";
    String DROPDOWNITEMS_NUMBER_OF_EMPLOYEES = "NUMBER_OF_EMPLOYEES";
    String DROPDOWNITEMS_ANNUAL_REVENUES = "ANNUAL_REVENUES";
    String DROPDOWNITEMS_CASEORIGINS = "CASEORIGINS";
    String DROPDOWNITEMS_TYPES = "TYPES";
    String DROPDOWNITEMS_PRIORITIES = "PRIORITIES";
    String DROPDOWNITEMS_CASEREASONS = "CASEREASONS";
    String DROPDOWNITEMS_RESOLVERS = "RESOLVERS";
    String DROPDOWNITEMS_CUSTOMFIELDS = "CUSTOMFIELDS";
    String DROPDOWNITEMS_MAILING_LIST = "MAILING_LIST";
    String DROPDOWNITEMS_STAGES = "STAGE";
    String DROPDOWNITEMS_LOCATIONS = "LOCATIONS";
    String DROPDOWNITEMS_VACANCIES = "VACANCIES";
}