delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT';
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT', 'PAYROLL', 'f', 'Attachment', (select max(sorder)+1 from permission where parent = (select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST')), (select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST'), 'TASK_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT', 'ALLOW', 'DR');


delete from "0".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT';
insert into "0".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT', 'PAYROLL');

delete from "0".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT';
insert into "0".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT', 'ALLOW', 'DR');








