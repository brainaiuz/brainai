

delete from permission where code = 'CRM_CONVERT_TO_CASE';
delete from permission where code = 'CRM_MC_CONVERT_TO_CASE';
insert into permission(code, context, name, sorder, parent, modulecode)
values ('CRM_MC_CONVERT_TO_CASE', 'CRM', 'Convert to Case', 12,
        (select id from permission where code = 'CRM_CASES_LIST'), 'CASE_MANAGEMENT');

delete from "0".rolepermission where permissioncode = 'CRM_CONVERT_TO_CASE';
delete from "0".rolepermission where permissioncode = 'CRM_MC_CONVERT_TO_CASE';
insert into "0".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_CASE', 'ALLOW', 'ADMIN');
insert into "0".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_CASE', 'ALLOW', 'DR');
insert into "0".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_CASE', 'ALLOW', 'ACCOUNTANT');

delete from "0".permission_context where permissioncode = 'CRM_CONVERT_TO_CASE';
delete from "0".permission_context where permissioncode = 'CRM_MC_CONVERT_TO_CASE';
insert into "0".permission_context(permissioncode, contextcode)
values ('CRM_MC_CONVERT_TO_CASE', 'CRM');
insert into "0".permission_context(permissioncode, contextcode)
values ('CRM_MC_CONVERT_TO_CASE', 'MESSAGECENTER');

delete from "anv".rolepermission where permissioncode = 'CRM_CONVERT_TO_CASE';
delete from "anv".rolepermission where permissioncode = 'CRM_MC_CONVERT_TO_CASE';
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_CASE', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_CASE', 'ALLOW', 'DR');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_CASE', 'ALLOW', 'ACCOUNTANT');

delete from "anv".permission_context where permissioncode = 'CRM_CONVERT_TO_CASE';
delete from "anv".permission_context where permissioncode = 'CRM_MC_CONVERT_TO_CASE';
insert into "anv".permission_context(permissioncode, contextcode)
values ('CRM_MC_CONVERT_TO_CASE', 'CRM');
insert into "anv".permission_context(permissioncode, contextcode)
values ('CRM_MC_CONVERT_TO_CASE', 'MESSAGECENTER');


delete from permission where code = 'CRM_MC_CONVERT_TO_LEAD';
insert into permission(code, context, name, sorder, parent, modulecode)
values ('CRM_MC_CONVERT_TO_LEAD', 'CRM', 'Convert to Lead', 16, (select id from permission where code = 'CRM_LEADS_LIST'), 'LEAD_MANAGEMENT');

delete from "0".rolepermission where permissioncode = 'CRM_MC_CONVERT_TO_LEAD';
insert into "0".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_LEAD', 'ALLOW', 'ADMIN');
insert into "0".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_LEAD', 'ALLOW', 'DR');
insert into "0".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_LEAD', 'ALLOW', 'ACCOUNTANT');

delete from "0".permission_context where permissioncode = 'CRM_MC_CONVERT_TO_LEAD';
insert into "0".permission_context(permissioncode, contextcode)
values ('CRM_MC_CONVERT_TO_LEAD', 'CRM');
insert into "0".permission_context(permissioncode, contextcode)
values ('CRM_MC_CONVERT_TO_LEAD', 'MESSAGECENTER');

delete from "anv".rolepermission where permissioncode = 'CRM_MC_CONVERT_TO_LEAD';
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_LEAD', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_LEAD', 'ALLOW', 'DR');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_LEAD', 'ALLOW', 'ACCOUNTANT');

delete from "anv".permission_context where permissioncode = 'CRM_MC_CONVERT_TO_LEAD';
insert into "anv".permission_context(permissioncode, contextcode)
values ('CRM_MC_CONVERT_TO_LEAD', 'CRM');
insert into "anv".permission_context(permissioncode, contextcode)
values ('CRM_MC_CONVERT_TO_LEAD', 'MESSAGECENTER');







