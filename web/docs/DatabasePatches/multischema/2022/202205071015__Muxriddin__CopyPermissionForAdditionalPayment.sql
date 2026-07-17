update permission set sorder = 11 where code = 'ADDITIONAL_PAYMENT_LINE_ITEM_DELETE';

delete from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_COPY';
insert into permission(code,context,name,sorder,parent,modulecode) values
					  ('PAYROLL_ADDITIONAL_PAYMENT_COPY','PAYROLL','Copy',12,(select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_COPY';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_COPY', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_COPY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_COPY', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_COPY', 'ALLOW', 'ACCOUNTANT');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_COPY', 'ALLOW', 'ADMIN');