

DELETE FROM permission WHERE code = 'PAYROLL_GROUP_PAYRUN_IMPORT';

insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('PAYROLL_GROUP_PAYRUN_IMPORT', 'PAYROLL', 'f', 'Import', '8',
(select id from permission where code='PAYROLL_GROUP_PAYRUN_LIST'), 'PAYROLL');

delete from  "anv".rolepermission where permissioncode='PAYROLL_GROUP_PAYRUN_IMPORT';
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('PAYROLL_GROUP_PAYRUN_IMPORT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('PAYROLL_GROUP_PAYRUN_IMPORT', 'DR', 'ALLOW');

delete from  "anv".permission_context where permissioncode='PAYROLL_GROUP_PAYRUN_IMPORT';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_GROUP_PAYRUN_IMPORT',  'PAYROLL');
