--parent: HRMS_CURENT_EMPLOYEE_PROFILE_TAB
--code: HRMS_PAYMENT_LIST


delete from permission where code = 'HRMS_PAYSLIP_LIST';

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_PAYSLIP_LIST', 'HRMS', false, 'Payslip list view', 40, (select id from permission where code = 'HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), false, 'HRMS_MODULE');

delete from "anv".rolepermission where permissioncode = 'HRMS_PAYSLIP_LIST';

insert into "anv".rolepermission (permissioncode, rolecode,access) values('HRMS_PAYSLIP_LIST','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('HRMS_PAYSLIP_LIST','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('HRMS_PAYSLIP_LIST','HR','ALLOW');

delete from "0".rolepermission where permissioncode = 'HRMS_PAYSLIP_LIST';

insert into "0".rolepermission (permissioncode, rolecode,access) values('HRMS_PAYSLIP_LIST','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('HRMS_PAYSLIP_LIST','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('HRMS_PAYSLIP_LIST','HR','ALLOW');