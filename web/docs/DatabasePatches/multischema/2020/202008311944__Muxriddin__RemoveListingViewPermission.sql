delete from permission where code='PM_TASK_LIST_VIEW' and modulecode='TASK_MANAGEMENT';
delete from permission where code='CRM_LEAD_LIST_VIEW' and modulecode='LEAD_MANAGEMENT';
delete from permission where code='CRM_OPPORTUNITY_LIST_VIEW' and modulecode='OPPORTUNITY_TRACKING';

delete from "anv".permission_context where permissioncode = 'PM_TASK_LIST_VIEW';
delete from "anv".permission_context where permissioncode = 'CRM_LEAD_LIST_VIEW';
delete from "anv".permission_context where permissioncode = 'CRM_OPPORTUNITY_LIST_VIEW';

delete from "anv".rolepermission where permissioncode = 'PM_TASK_LIST_VIEW';
delete from "anv".rolepermission where permissioncode = 'CRM_LEAD_LIST_VIEW';
delete from "anv".rolepermission where permissioncode = 'CRM_OPPORTUNITY_LIST_VIEW';
