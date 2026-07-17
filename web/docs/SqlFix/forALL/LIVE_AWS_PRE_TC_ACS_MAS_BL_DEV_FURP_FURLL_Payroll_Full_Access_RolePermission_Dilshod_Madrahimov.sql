---- for public schema --
delete from permission where code = 'PAYROLL_SINGLE_PAYRUN_FULL_ACCESS';
delete from permission where code = 'PAYROLL_GROUP_PAYRUN_FULL_ACCESS';
delete from permission where code = 'PAYROLL_CASH_ADVANCE_FULL_ACCESS';
delete from permission where code = 'PAYROLL_EMPLOYEES_FULL_ACCESS';
delete from permission where code = 'PAYROLL_GROUP_FULL_ACCESS';

update permission set code='PAYROLL_GROUP_LIST',name='Payroll Group List' where code='PAYROLL_BATCH_LIST';
update permission set code='PAYROLL_GROUP_ADD',name='Payroll Group Add' where code='PAYROLL_BATCH_ADD';
update permission set code='PAYROLL_GROUP_DELETE',name='Payroll Group Delete' where code='PAYROLL_GROUP_DELETE';

insert into permission(code,context,name,sorder,ismainmenu,parent,iscore,modulecode)
values('PAYROLL_SINGLE_PAYRUN_FULL_ACCESS','PAYROLL','Single Payrun Full Access',(SELECT max(sorder)+1 from permission WHERE code='PAYROLL_PAYSLIP_LIST'),'f',(SELECT ID FROM PERMISSION WHERE CODE = 'PAYROLL_PAYSLIP_LIST'),'f','PAYROLL');

insert into permission(code,context,name,sorder,ismainmenu,parent,iscore,modulecode)
values('PAYROLL_GROUP_PAYRUN_FULL_ACCESS','PAYROLL','Group Payrun Full Access',(SELECT max(sorder)+1 from permission WHERE code='PAYROLL_GROUP_PAYRUN_LIST'),'f',(SELECT ID FROM PERMISSION WHERE CODE = 'PAYROLL_GROUP_PAYRUN_LIST'),'f','PAYROLL');

insert into permission(code,context,name,sorder,ismainmenu,parent,iscore,modulecode)
values('PAYROLL_CASH_ADVANCE_FULL_ACCESS','PAYROLL','Cash Advance Full Access',(SELECT max(sorder)+1 from permission WHERE code='PAYROLL_CASH_ADVANCE_LIST'),'f',(SELECT ID FROM PERMISSION WHERE CODE = 'PAYROLL_CASH_ADVANCE_LIST'),'f','PAYROLL');

insert into permission(code,context,name,sorder,ismainmenu,parent,iscore,modulecode)
values('PAYROLL_EMPLOYEES_FULL_ACCESS','PAYROLL','Employees Full Access',(SELECT max(sorder)+1 from permission WHERE code='PAYROLL_EMPLOYEES_LIST'),'f',(SELECT ID FROM PERMISSION WHERE CODE = 'PAYROLL_EMPLOYEES_LIST'),'f','PAYROLL');

insert into permission(code,context,name,sorder,ismainmenu,parent,iscore,modulecode)
values('PAYROLL_GROUP_FULL_ACCESS','PAYROLL','Payroll Group Full Access',(SELECT max(sorder)+1 from permission WHERE code='PAYROLL_GROUP_LIST'),'f',(SELECT ID FROM PERMISSION WHERE CODE = 'PAYROLL_GROUP_LIST'),'f','PAYROLL');

--- for private schema --
delete from "anv".rolepermission where permissioncode = 'PAYROLL_SINGLE_PAYRUN_FULL_ACCESS';
delete from "anv".rolepermission where permissioncode = 'PAYROLL_GROUP_PAYRUN_FULL_ACCESS';
delete from "anv".rolepermission where permissioncode = 'PAYROLL_CASH_ADVANCE_FULL_ACCESS';
delete from "anv".rolepermission where permissioncode = 'PAYROLL_EMPLOYEES_FULL_ACCESS';
delete from "anv".rolepermission where permissioncode = 'PAYROLL_GROUP_FULL_ACCESS';

update "anv".rolepermission set permissioncode='PAYROLL_GROUP_LIST' where permissioncode='PAYROLL_BATCH_LIST';
update "anv".rolepermission set permissioncode='PAYROLL_GROUP_ADD' where permissioncode='PAYROLL_BATCH_ADD';
update "anv".rolepermission set permissioncode='PAYROLL_GROUP_DELETE' where permissioncode='PAYROLL_GROUP_DELETE';

insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_SINGLE_PAYRUN_FULL_ACCESS','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_SINGLE_PAYRUN_FULL_ACCESS','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_SINGLE_PAYRUN_FULL_ACCESS','ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_PAYRUN_FULL_ACCESS','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_PAYRUN_FULL_ACCESS','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_PAYRUN_FULL_ACCESS','ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_CASH_ADVANCE_FULL_ACCESS','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_CASH_ADVANCE_FULL_ACCESS','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_CASH_ADVANCE_FULL_ACCESS','ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEES_FULL_ACCESS','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEES_FULL_ACCESS','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEES_FULL_ACCESS','ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_FULL_ACCESS','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_FULL_ACCESS','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_FULL_ACCESS','ACCOUNTANT','ALLOW');


--- for ZERO schema --
delete from "0".rolepermission where permissioncode = 'PAYROLL_SINGLE_PAYRUN_FULL_ACCESS';
delete from "0".rolepermission where permissioncode = 'PAYROLL_GROUP_PAYRUN_FULL_ACCESS';
delete from "0".rolepermission where permissioncode = 'PAYROLL_CASH_ADVANCE_FULL_ACCESS';
delete from "0".rolepermission where permissioncode = 'PAYROLL_EMPLOYEES_FULL_ACCESS';

update "0".rolepermission set permissioncode='PAYROLL_GROUP_LIST' where permissioncode='PAYROLL_BATCH_LIST';
update "0".rolepermission set permissioncode='PAYROLL_GROUP_ADD' where permissioncode='PAYROLL_BATCH_ADD';
update "0".rolepermission set permissioncode='PAYROLL_GROUP_DELETE' where permissioncode='PAYROLL_GROUP_DELETE';

insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_SINGLE_PAYRUN_FULL_ACCESS','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_SINGLE_PAYRUN_FULL_ACCESS','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_SINGLE_PAYRUN_FULL_ACCESS','ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_PAYRUN_FULL_ACCESS','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_PAYRUN_FULL_ACCESS','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_PAYRUN_FULL_ACCESS','ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_CASH_ADVANCE_FULL_ACCESS','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_CASH_ADVANCE_FULL_ACCESS','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_CASH_ADVANCE_FULL_ACCESS','ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEES_FULL_ACCESS','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEES_FULL_ACCESS','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEES_FULL_ACCESS','ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_FULL_ACCESS','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_FULL_ACCESS','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_GROUP_FULL_ACCESS','ACCOUNTANT','ALLOW');

