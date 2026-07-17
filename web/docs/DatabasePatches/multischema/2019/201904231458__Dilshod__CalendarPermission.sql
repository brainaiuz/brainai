-- zero
delete from "0".permission_context where permissioncode = 'CRM_Calendar';

insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','CRM');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','ACCOUNTING');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','DASHBOARD');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','DOCUMENTS');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','HRMS');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','LOGISTICS');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','MYACCOUNT');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','MYWORKSPACE');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','PAYROLL');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','PM');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','REPORTING');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','SETTINGS');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','TRAININGCENTER');
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_Calendar','WORKSPACE');


---anv
delete from "anv".permission_context where permissioncode = 'CRM_Calendar';

insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','CRM');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','ACCOUNTING');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','DASHBOARD');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','DOCUMENTS');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','HRMS');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','LOGISTICS');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','MYACCOUNT');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','MYWORKSPACE');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','PAYROLL');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','PM');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','REPORTING');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','SETTINGS');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','TRAININGCENTER');
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_Calendar','WORKSPACE');
