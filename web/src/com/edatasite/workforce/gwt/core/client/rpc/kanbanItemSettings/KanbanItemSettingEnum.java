package com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings;

public enum KanbanItemSettingEnum {
    // sections
    TASK_ITEM_SETTINGS("Task Kanban Item", "TASK_ITEM_SETTINGS"),
    OPPORTUNITY_ITEM_SETTINGS("Opportunity Kanban Item", "OPPORTUNITY_ITEM_SETTINGS"),
    LEAD_ITEM_SETTINGS("Lead Kanban Item", "LEAD_ITEM_SETTINGS"),
    CASE_ITEM_SETTINGS("Case Kanban Item", "CASE_ITEM_SETTINGS"),
    CANDIDATE_ITEM_SETTINGS("Candidate Kanban Item", "CANDIDATE_ITEM_SETTINGS"),

    // Task-fields

    TASK_NAME("Name", "TASK_NAME"),
    TASK_CODE("Code", "TASK_CODE"),
    TASK_START_DATE("Start Date", "TASK_START_DATE"),
    TASK_CUSTOMER_NAME ("Customer Name", "TASK_CUSTOMER_NAME"),
    TASK_END_DATE("End Date", "TASK_END_DATE"),
    TASK_ASSIGNEE_EMPLOYEE("Assignee Employee", "TASK_ASSIGNEE_EMPLOYEE"),
    TASK_PROJECTNAME("Project Name", "TASK_PROJECTNAME"),
    TASK_DESCRIPTION("Description", "TASK_DESCRIPTION"),
    TASK_PRIORITY("Priority", "TASK_PRIORITY"),
    TASK_ACTION("Action", "TASK_ACTION"),

    // Opportunity-fields
    OPPORTUNITY_NAME("Name", "OPPORTUNITY_NAME"),
    OPPORTUNITY_AMOUT("Amount", "OPPORTUNITY_AMOUT"),
    OPPORTUNITY_ENTRY_PHOTO("Entry photo", "OPPORTUNITY_ENTRY_PHOTO"),
    OPPORTUNITY_EMAIL("Email", "OPPORTUNITY_EMAIL"),
    OPPORTUNITY_PHONE("Phone", "OPPORTUNITY_PHONE"),
    OPPORTUNITY_ASSIGNE_NAME("Assigne Name", "OPPORTUNITY_ASSIGNE_NAME"),
    OPPORTUNITY_BACKUP_ASSIGNE_NAME("Backup Assigne Name", "OPPORTUNITY_BACKUP_ASSIGNE_NAME"),
    OPPORTUNITY_INFO("Info", "OPPORTUNITY_INFO"),
    OPPORTUNITY_CLOSEDATE("Close Date", "OPPORTUNITY_CLOSEDATE"),
    OPPORTUNITY_CONTACT("Contact", "OPPORTUNITY_CONTACT"),
    OPPORTUNITY_CONTACT_PHONE("Contact Phone", "OPPORTUNITY_CONTACT_PHONE"),
    OPPORTUNITY_ACTION("Action", "OPPORTUNITY_ACTION"),
    OPPORTUNITY_NOTE("Note", "OPPORTUNITY_NOTE"),

    // Lead-fields
    LEAD_NAME("Name", "LEAD_NAME"),
    LEAD_ENTRY_PHOTO("Entry photo", "LEAD_ENTRY_PHOTO"),
    LEAD_EMAIL("Email", "LEAD_EMAIL"),
    LEAD_PHONE("Phone", "LEAD_PHONE"),
    LEAD_ASSIGNE_NAME("Assigne Name", "LEAD_ASSIGNE_NAME"),
    LEAD_INFO("Info", "LEAD_INFO"),
    LEAD_ACTION("Action", "LEAD_ACTION"),
    LEAD_NOTE("Note", "LEAD_NOTE"),

    // Case-fields
    CASE_NAME("Name", "CASE_NAME"),
    CASE_NUMBER("Number", "CASE_NUMBER"),
    CASE_SUBJECT("Subject", "CASE_SUBJECT"),
    CASE_REPORTER("Reporter", "CASE_REPORTER"),
    CASE_PHONE("Phone", "CASE_PHONE"),
    CASE_EMAIL("Email", "CASE_EMAIL"),
    CASE_ASSIGNE_NAME("Assignee Name", "CASE_ASSIGNE_NAME"),
    CASE_ACTION("Action", "CASE_ACTION"),
    CASE_NOTE("Note", "CASE_NOTE"),


    // Candidate-fields
    CANDIDATE_NAME("Name", "CANDIDATE_NAME"),
    CANDIDATE_LEAD_NAME("Lead Name", "CANDIDATE_LEAD_NAME"),
    CANDIDATE_PHONE("Phone", "CANDIDATE_PHONE"),
    CANDIDATE_EMAIL("Email", "CANDIDATE_EMAIL"),
    CANDIDATE_LEAD_ASSIGNEE("Lead Assignee", "CANDIDATE_LEAD_ASSIGNEE"),
    CANDIDATE_ACTION("Action", "CANDIDATE_ACTION"),
    CANDIDATE_NOTE("Note", "CANDIDATE_NOTE"),
    CANDIDATE_POSITION("Position","CANDIDATE_POSITION"),
    CANDIDATE_DEPARTMENT("Department","CANDIDATE_DEPARTMENT"),
    CANDIDATE_LOCATION("Location","CANDIDATE_LOCATION");

    private final String name;
    private final String code;

    KanbanItemSettingEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static KanbanItemSettingEnum getEnumByCode(String enumCode) {
        if (enumCode == null) return null;
        KanbanItemSettingEnum settingEnum = null;
        for (KanbanItemSettingEnum enumItem : KanbanItemSettingEnum.values()) {
            if (enumCode.equals(enumItem.getCode())) {
                settingEnum = enumItem;
            }
        }
        return settingEnum;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
