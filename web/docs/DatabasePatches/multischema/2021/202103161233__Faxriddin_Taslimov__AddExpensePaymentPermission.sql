delete
from permission
where code = 'ACCOUNTING_EXPENSE_ADD_PAYMENT';
delete
from "anv".rolepermission
where permissioncode = 'ACCOUNTING_EXPENSE_ADD_PAYMENT';
delete
from "anv".permission_context
where permissioncode = 'ACCOUNTING_EXPENSE_ADD_PAYMENT';

insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_EXPENSE_ADD_PAYMENT', 'ACCOUNTING', 'Add Payment', 5, (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), 'EXPENSE_REPORTING');

insert into "anv".permission_context (permissioncode, contextcode)
values ('ACCOUNTING_EXPENSE_ADD_PAYMENT', 'ACCOUNTING');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_EXPENSE_ADD_PAYMENT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_EXPENSE_ADD_PAYMENT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_EXPENSE_ADD_PAYMENT', 'ALLOW', 'ACCOUNTANT');


delete
from permission
where code = 'HRMS_EXPENSE_ADD_PAYMENT';
delete
from "anv".rolepermission
where permissioncode = 'HRMS_EXPENSE_ADD_PAYMENT';
delete
from "anv".permission_context
where permissioncode = 'HRMS_EXPENSE_ADD_PAYMENT';

insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_EXPENSE_ADD_PAYMENT', 'HRMS', 'Add Payment', 5, (select id from permission where code = 'HRMS_EXPENCE_REPORT'), 'EMPLOYEE_EXPENSES');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_EXPENSE_ADD_PAYMENT', 'HRMS');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_EXPENSE_ADD_PAYMENT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_EXPENSE_ADD_PAYMENT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_EXPENSE_ADD_PAYMENT', 'ALLOW', 'ACCOUNTANT');
