

delete from permission where code in ('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'PM_SHOW_EMPLOYMENT_INFORMATION', 'PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'HRMS_SHOW_ADDITIONAL_INFORMATION', 'PM_SHOW_ADDITIONAL_INFORMATION');
UPDATE permission set sorder=25 where code='HRMS_ALL_EMPLOYEE_BONUS_RECOMMENDATIONS';
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'HRMS', 'Show Employment Information', 4, false, (select id from permission where code='HRMS_EMPLOYEE_PROFILE'), false,  'HRMS_MODULE');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'HRMS', 'Show Own Employment Information', 4, false, (select id from permission where code='HRMS_EMPLOYEE_PROFILE_SUMMARY'), false,  'HRMS_MODULE');

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('HRMS_SHOW_ADDITIONAL_INFORMATION', 'HRMS', 'Show Additional Information', 5, false, (select id from permission where code='HRMS_EMPLOYEE_PROFILE'), false,  'HRMS_MODULE');

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('PM_SHOW_EMPLOYMENT_INFORMATION', 'PM', 'Show Employment Information', 16, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'PM', 'Show Own Employment Information', 17, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('PM_SHOW_ADDITIONAL_INFORMATION', 'PM', 'Show Additional Information', 18, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');


delete from "0".rolepermission where permissioncode in ('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'PM_SHOW_EMPLOYMENT_INFORMATION', 'PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'HRMS_SHOW_ADDITIONAL_INFORMATION', 'PM_SHOW_ADDITIONAL_INFORMATION');
--HRMS
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'TL', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'ADMIN_LOCATION', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'MEM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_ADDITIONAL_INFORMATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_ADDITIONAL_INFORMATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_ADDITIONAL_INFORMATION', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_ADDITIONAL_INFORMATION', 'MEM', 'ALLOW');
--PM
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'TL', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'ADMIN_LOCATION', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'MEM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ADDITIONAL_INFORMATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ADDITIONAL_INFORMATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ADDITIONAL_INFORMATION', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ADDITIONAL_INFORMATION', 'MEM', 'ALLOW');



delete from "anv".rolepermission where permissioncode in ('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'PM_SHOW_EMPLOYMENT_INFORMATION', 'PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'HRMS_SHOW_ADDITIONAL_INFORMATION', 'PM_SHOW_ADDITIONAL_INFORMATION');
--HRMS
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'TL', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYMENT_INFORMATION', 'ADMIN_LOCATION', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION', 'MEM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_ADDITIONAL_INFORMATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_ADDITIONAL_INFORMATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_ADDITIONAL_INFORMATION', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_ADDITIONAL_INFORMATION', 'MEM', 'ALLOW');
--PM
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'TL', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYMENT_INFORMATION', 'ADMIN_LOCATION', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_OWN_EMPLOYMENT_INFORMATION', 'MEM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ADDITIONAL_INFORMATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ADDITIONAL_INFORMATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ADDITIONAL_INFORMATION', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ADDITIONAL_INFORMATION', 'MEM', 'ALLOW');
