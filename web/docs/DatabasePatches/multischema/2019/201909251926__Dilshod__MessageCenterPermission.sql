delete  from context where code='MESSAGECENTER';
insert into context(code) values ('MESSAGECENTER');

delete from "0".permission_context where permissioncode='CRM_MESSAGE_CENTER' and contextcode='MESSAGECENTER';
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_MESSAGE_CENTER','MESSAGECENTER');


delete from "0".permission_context where permissioncode='CRM_CASES_LIST' and contextcode='MESSAGECENTER';
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_CASES_LIST','MESSAGECENTER');

delete from "0".permission_context where permissioncode='CRM_LEADS_LIST' and contextcode='MESSAGECENTER';
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_LEADS_LIST','MESSAGECENTER');

delete from "anv".permission_context where permissioncode='CRM_CASES_LIST' and contextcode='MESSAGECENTER';
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_CASES_LIST','MESSAGECENTER');

delete from "anv".permission_context where permissioncode='CRM_LEADS_LIST' and contextcode='MESSAGECENTER';
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_LEADS_LIST','MESSAGECENTER');

