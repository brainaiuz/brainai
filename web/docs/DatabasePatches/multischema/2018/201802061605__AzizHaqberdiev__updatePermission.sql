update permission set modulecode = 'CRM_MODULE' where modulecode = 'EMAIL_MARKETING';

delete from modelfield where form_id = 'CASE_FORM' and field_id in ('NOTIFY_REPORTER','SLA','BILLABLE','SLA_TIMER','CRM_ACTIVITIES','CRM_TASKS');
delete from "0".modelfield where form_id = 'CASE_FORM' and field_id in ('NOTIFY_REPORTER','SLA','BILLABLE','SLA_TIMER','CRM_ACTIVITIES','CRM_TASKS');

delete from "anv".myModule where code = 'EMAIL_MARKETING';
delete from "anv".modelfield where form_id = 'CASE_FORM' and field_id in ('NOTIFY_REPORTER','SLA','BILLABLE','SLA_TIMER','CRM_ACTIVITIES','CRM_TASKS');

delete from "0".genericSettings where key in ('CRM_ACCOUNT_TREE_ENABLED','IS_MINDSHARE_OR_POP_COMPANY');
delete from "anv".genericSettings where key in ('CRM_ACCOUNT_TREE_ENABLED','IS_MINDSHARE_OR_POP_COMPANY');
