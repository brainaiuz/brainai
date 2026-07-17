package com.edatasite.workforce.gwt.core.client.ui.resourceUtil;

/**
 * User: Ilhombek
 * Date: 5/22/12
 * Time: 2:13 PM
 */
public interface ResourceUtilReportConstants {

    String STYLE_CURRENT_DAY = "current-day";//TD style
    String STYLE_TOTAL_DAY = "total-day";//TD style
    String STYLE_TOTAL_MIDDLE_DAY = "total-middle-day";//TD style
    //sunday
    String STYLE_SUNDAY_MONTH = "weekend-month";//TD style                     //sunday with empty data
    String STYLE_SUNDAY_RESOURCE_MONTH = "weekend-month-resource";//TD style	//sunday with time
    //
    String STYLE_WORK_MONTH_HOLIDAY = "work-month-holiday";//TD style
    String STYLE_WORK_MONTH_RESOURCE_HOLIDAY = "work-month-holiday-resource";//TD style
    //holiday
    String STYLE_WORK_COMPANY_HOLIDAY = "work-company-holiday";//TD style                   //holiday day - with H
    String STYLE_WORK_COMPANY_RESOURCE_HOLIDAY = "work-company-holiday-resource";//TD style //holiday day - with time
    //LR day
    String STYLE_WITH_LR_DAY = "with-lr-day";//TD style
    String STYLE_WITH__UNAUTHORIZED_LR_DAY = "with-unauthoruized-lr-day";//TD style

    String STYLE_MONTH_DAY = "month-day";//TD style
    //sample day
    String STYLE_DEFAULT_MONTH_DAY = "default-month-day";//TD style
    String STYLE_DEFAULT_MONTH_DAY_PRO_NAME = "default-month-day-p";//TD style
    String STYLE_DEFAULT_MONTH_OPTIMALLY_ALLOCATED_DAY = "default-month-optimally-day";//TD style
    String STYLE_DEFAULT_MONTH_OVER_ALLOCATED_DAY = "default-month-over-day";//TD style
    String STYLE_DEFAULT_MONTH_UNDER_ALLOCATED_DAY = "default-month-under-day";//TD style

    //working days
    String STYLE_WORKING_DAY_EMPLOYEE_NAME = "working-day-employee";//TD style    //working day Employee style
    String STYLE_WORKING_DAY_PROJECT_NAME = "working-day-project";//TD style      //working day Project style
    String STYLE_WORKING_DAY_TASK_NAME = "working-day-task";//TD style            //working day Task style

    //class names
    String CLASS_RESOURCE_UTILREPORT_BIG_Table = "resourceUtilReportBigTable";//table style
    String CLASS_RESOURCE_UTIL_T = "rsrcUtlzTbl";//table style

    //top panel style names
    String CLASS_TO_LEFT_Table = "toLeftTable";//table style
    String CLASS_FULL_WITH_ = "fullWidth";//table style

    String CLASS_RESOURCE_REPORT_HLeft = "resourceReportHL";//TD style
    String CLASS_RESOURCE_REPORT_HRight = "resourceReportHR";//TD style
    String CLASS_RESOURCE_REPORT_HRight_MONTH = "resourceReportHRMONTH";//span style

    //for employee name tr/th/td class names
    String CLASS_EMPLOYEE_NAME_TR = "GELY21NME";//TR style
    String CLASS_EMPLOYEE_NAME_TH = "resourceEmpNaME";//TH style
    //for time slot hours tr/th/td class names
    String CLASS_TIME_SLOT_HOURS_TR_VISIBLE = "timeSTVIS";//table style
    String CLASS_TIME_SLOT_HOURS_TR_V = "GTE17STHR2";//TR style
    String CLASS_TIME_SLOT_HOURS_TR = "GTE17STHR";//TR style
    String CLASS_TIME_SLOT_HOURS_TH = "timeSTHourNaME";//TH style

    //for over all oi/out hours tr/th/td class names
    String CLASS_IN_OUT_HOURS_TR_V = "GOEALTM27SEHS2";//TR style
    String CLASS_IN_OUT_HOURS_TH = "timeSTHourNaME";//TH style
    String CLASS_IN_OUT_HOURS_TR = "GTE17STHR";//TR style

    //for over all time sheet hours tr/th/td class names
    String CLASS_OVERALL_TIME_SHEET_HOURS_TR_V = "GOEALTM27SEHS2";//TR style
    String CLASS_OVERALL_TIME_SHEET_HOURS_TR = "GOEALTM27SEHS";//TR style
    String CLASS_OVERALL_TIME_SHEET_HOURS_TH = "overAllTimeSHTHourNaME";//TH style
    //for LR hours and Holiday days tr/th/td class names
    String CLASS_LR_HOURS_VS_HOLIDAY_DAYS_TR_V = "GLRH31HIDDY2";//TR style
    String CLASS_LR_HOURS_VS_HOLIDAY_DAYS_TR = "GLRH31HIDDY";//TR style
    String CLASS_LR_HOURS_VS_HOLIDAY_DAYS_TH = "lrHoursNaME";//TH style
    //for time sheet hours tr/th/td class names
    String CLASS_TIME_SHEET_HOURS_TR_VISIBLE = "timeSHVIS";//table style
    String CLASS_TIME_SHEET_HOURS_TR_V = "GTE19SHTH2";//TR style
    String CLASS_TIME_SHEET_HOURS_TR = "GTE19SHTH";//TR style
    String CLASS_TIME_SHEET_HOURS_TH = "timeSHTHourNaME";//TH style
    //for employee project/task tr/td class names
    String CLASS_EMPLOYEE_PROJECT_NAME_TR_VISIBLE = "empLProjVIS";//table style
    String CLASS_EMPLOYEE_PROJECT_NAME_TR_V = "GEPOE23PJET2";//TR style
    String CLASS_EMPLOYEE_PROJECT_NAME_TR = "GEPOE23PJET";//TR style
    String CLASS_EMPLOYEE_PROJECT_NAME_TH = "projNaME";//TH style
    //for employee task tr/th class names
    String CLASS_EMPLOYEE_TASK_NAME_TR_VISIBLE = "empLTaskVIS";//table style
    String CLASS_EMPLOYEE_TASK_NAME_TR_V = "GMLEE25ASK2";//TR style
    String CLASS_EMPLOYEE_TASK_NAME_TR = "GMLEE25ASK";//TR style
    String CLASS_EMPLOYEE_TASK_NAME_TH = "taskNaME";//TH style


    //for expanded/collapsed option class name
    String CLASS_EXPANDED_ELEMENT = "expandedElement";//TD style

    //for
    String CLASS_COLLAPSED_EMPLOYEE_NAME = "collEMPNAME";
    String CLASS_EXPANDED_EMPLOYEE_NAME = "expEMPNAME";
    String CLASS_COLLAPSED_PROJECT_NAME = "collPRONAME";
    String CLASS_EXPANDED_PROJECT_NAME = "expPRONAME";

    //
    String CLASS_REPLACED_ELEMENT = "replacedElemST";

    int PAGE_SIZE = 10;
}