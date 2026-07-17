delete from permission where code = 'SETTINGS_BENEFIT_ALLOWANCE';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('SETTINGS_BENEFIT_ALLOWANCE',   'SETTINGS', false, 'Benefit Allowance', 30, (select id from permission where code = 'SETTINGS_HRMS_SETTINGS'), FALSE, 'ATTENDING_TRACKING');

delete from "anv".permission_context where permissioncode = 'SETTINGS_BENEFIT_ALLOWANCE';
insert into "anv".permission_context(permissioncode,contextcode) values ('SETTINGS_BENEFIT_ALLOWANCE','SETTINGS');

delete from "anv".rolepermission where permissioncode = 'SETTINGS_BENEFIT_ALLOWANCE';
insert into "anv".rolepermission(permissioncode, rolecode, access) values ('SETTINGS_BENEFIT_ALLOWANCE', 'HR', 'ALLOW');

delete from permission where code = 'SETTINGS_APPRAISAL_SETTINGS';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('SETTINGS_APPRAISAL_SETTINGS',   'SETTINGS', false, 'Appraisal Settings', 35, (select id from permission where code = 'SETTINGS_HRMS_SETTINGS'), FALSE, 'ATTENDING_TRACKING');

delete from "anv".permission_context where permissioncode = 'SETTINGS_APPRAISAL_SETTINGS';
insert into "anv".permission_context(permissioncode,contextcode) values ('SETTINGS_APPRAISAL_SETTINGS','SETTINGS');

delete from "anv".rolepermission where permissioncode = 'SETTINGS_APPRAISAL_SETTINGS';
insert into "anv".rolepermission(permissioncode, rolecode, access) values ('SETTINGS_APPRAISAL_SETTINGS', 'HR', 'ALLOW');

delete from permission where code = 'SETTINGS_VALIDITY_PERIODS';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('SETTINGS_VALIDITY_PERIODS',   'SETTINGS', false, 'Validity Periods', 40, (select id from permission where code = 'SETTINGS_HRMS_SETTINGS'), FALSE, 'ATTENDING_TRACKING');

delete from "anv".permission_context where permissioncode = 'SETTINGS_VALIDITY_PERIODS';
insert into "anv".permission_context(permissioncode,contextcode) values ('SETTINGS_VALIDITY_PERIODS','SETTINGS');

delete from "anv".rolepermission where permissioncode = 'SETTINGS_VALIDITY_PERIODS';
insert into "anv".rolepermission(permissioncode, rolecode, access) values ('SETTINGS_VALIDITY_PERIODS', 'HR', 'ALLOW');
