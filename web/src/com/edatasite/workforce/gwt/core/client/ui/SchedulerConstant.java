package com.edatasite.workforce.gwt.core.client.ui;

/**
 * User: Administrator
 * Date: Apr 23, 2010
 * Time: 10:48:52 AM
 */
public interface SchedulerConstant {
    int RECURRENCE_TYPE_DAILY = 1;
    int RECURRENCE_TYPE_WEEKLY = 2;
    int RECURRENCE_TYPE_MONTHLY = 3;
    int RECURRENCE_TYPE_YEARLY = 4;
    int RECURRENCE_TYPE_HOURLY = 5;
    int RECURRENCE_TYPE_MINUTELY = 6;
    int RECURRENCE_TYPE_EVEN_DAILY = 7;
    int RECURRENCE_TYPE_ODD_DAILY = 8;

    String WEEKDAYS = " MON-FRI";
    String WEEKENDS = " SUN,SAT";

    String ALL = " *";
    String NO_SPECIFIC_VALUE = " ?";

    int NO_END_DATE = 1;
    int END_AFTER_OCCURRENCES = 2;
    int END_BY_DATE = 3;

    int DAILY_PATTERN_OPTION_INTERVAL = 1;
    int DAILY_PATTERN_OPTION_WEEKDAYS = 2;
    int DAILY_PATTERN_OPTION_WEEKENDS = 3;

    int MONTHLY_OR_YEARLY_PATTERN_SIMPLE = 1;
    int MONTHLY_OR_YEARLY_PATTERN_CUSTOM = 2;

    String CURRENT_DAY = "Current day";
    String PREVIOUS_DAY = "Previous day";
    int FIRST = 1;
    int SECOND = 2;
    int THIRD = 3;
    int FOURTH = 4;
    int LAST = 5;

    int SUNDAY = 1;
    int MONDAY = 2;
    int TUESDAY = 3;
    int WEDNESDAY = 4;
    int THURSDAY = 5;
    int FRIDAY = 6;
    int SATURDAY = 7;

    int MINUTES = 10;
    int CREATE_EVENT_LIMIT = 50;
    int CREATE_EVENT_INDEX = 10;

    ///////////////////// RECURRENCE JOBS /////////////////////////////////////

    String TRIGGER_NAME = "triggerName";
    String TRIGGER_GROUP = "triggerGroup";
    String JOB_NAME = "jobName";
    String JOB_GROUP = "jobGroup";

    // for Timesheet Reminder;
    String FORCURRENT = "forCurrent";
    String FORPREVIOUS = "forPrevious";

    // type of recurring form
    int INVOICE_ADD_FORM = 1;
    int RECURRING_TASK_FORM = 2;
    int RECURRING_EVENT_FORM = 3;
    int OVERDUE_INVOICE_FORM = 4;
    int RECURRING_REPORT_FORM = 5;
    int RECURRING_BILL_FORM = 6;
    int RECURRING_MANUAL_JOURNAL_FORM = 7;
    int RECURRING_WORKFLOW_FORM = 8;
    int RECURRING_MAGENTO_FORM = 9;
    int RECURRING_TIMESHEET_FORM = 10;
    int RECURRING_COURSE_SCHEDULE_FORM = 11;

    // for recurrence job type (EdsRecurrenceJob class : jobType field)
    int OVERDUE_INVOICE_REMINDER = 1;
    int TIMESHEET_REMINDER = 2;
    int CALENDAR_EVENT_REMINDER = 3;
    int RECURRING_INVOICE_REMINDER = 4;
    int TASK_OVERDUE_REMINDER = 5;
    int SYNCHRONIZE_GOOGLE_CONTACT = 6;
    int SYNCHRONIZE_GOOGLE_CALENDAR = 7;
    int MASS_MAILING_RECURRENCE = 8;
    int RECURRING_TASK = 9;
    int RECURRING_EVENT = 10;
    int RECURRING_REPORT = 11;
    int RECURRING_HOLIDAY = 12;
    int RECURRING_PROJECT_NUMBER = 13;
    int RECURRING_BACKEND_STATISTIC = 14;
    int RECURRING_ASSESSMENT_REMINDER = 15;
    int EMPLOYEE_VISA_EXPIRATION_REMINDER = 16;
    int MESSAGE_CENTER_EMAIL_FETCHING = 17;
    int RECURRING_GWD_SYNC = 18;
    int PROJECT_OVERDUE_REMINDER = 19;
    int WORKSTREAM_OVERDUE_REMINDER = 20;
    int WORKFLOW_RECURRENCE = 21;
    int RECURRING_BILL_REMINDER = 22;
    int RECURRING_MANUAL_JOURNAL_REMINDER = 23;
    int DAILY_HR_REMINDERP_ROCEDURE_JOB = 24;
    int RECURRING_WORKFLOW = 25;
    int CONTRACT_OVERDUE_REMINDER = 26;
    int DAILY_FILL_TIMESHEET_FROM_RES_UTIL = 27;
    int SYNCHRONIZE_OFFICE_CALENDAR = 28;
    int SYNCHRONIZE_MAGENTO_CATALOG = 29;
    int RECURRING_DEFERRED_TRANSACTIONS = 31;//DO NOT CHANGE ITS VALUE TO 30, CAUSE OF THIS, ON DEV LAST JOB IS 29 WHEREAS ON LIVE 30
    int RECURRING_LEAVE_REQUEST_NUMBER = 32;
    int RECURRING_ADDITIONAL_PAYMENT = 33;
    int RECURRING_COURSE_SCHEDULE = 34;
    // for recurrence status field
    String SUCCESS = "SUCCESS";
    String FAIL = "FAIL";
    String IN_PROGRESS = "IN_PROGRESS";

    // for recurrence business object type (EdsRecurrenceJob class : busObjectType field)
    Integer RJ_COMPANY_ID = 1;
    Integer RJ_EMPLOYEE_ID = 2;
    Integer RJ_EVENT_ID = 3;
    Integer RJ_TASK_ID = 4;
    Integer RJ_MAIL_MESSAGE_ID = 5;

    // JobDataMap parameters
    String LATE_FIRE_TIME = "lateFireTime";
    String BUS_OBJECT_ID = "busObjectId";
    String BUS_OBJECT_PARAM = "busObjectParam";
    String COMPANY_ID = "companyID";
    String USER_ID = "userID";
    String CATEGORY = "category";
    String TO_ME = "toMe";
    String TO_CLIENT = "toClient";
    String REC_OBJECT_ID = "recObjectId";
    String START_DATE = "startDate";
    String END_TYPE = "endType";
    String TYPE = "type";
    String JOB_TYPE_STRING = "jobTypeString";
    String CRON_EXPRESSION = "cronExpression";
    String RECURRENCE_HISTORY_ID = "recurrenceHistoryID";
}
