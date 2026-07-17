insert into permission(code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_EXPENSE_EDIT_PAYMENT', 'ACCOUNTING', 'Edit Payment', 6,
        (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), 'EXPENSE_REPORTING');
insert into permission(code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_EXPENSE_DELETE_PAYMENT', 'ACCOUNTING', 'Delete Payment', 7,
        (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), 'EXPENSE_REPORTING');
insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_EXPENSE_EDIT_PAYMENT', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_EXPENSE_DELETE_PAYMENT', 'ACCOUNTING');

insert into permission(code, context, name, sorder, parent, modulecode)
values ('HRMS_EXPENSE_EDIT_PAYMENT', 'HRMS', 'Edit Payment', 6,
        (select id from permission where code = 'HRMS_EXPENCE_REPORT'), 'EMPLOYEE_EXPENSES');
insert into permission(code, context, name, sorder, parent, modulecode)
values ('HRMS_EXPENSE_DELETE_PAYMENT', 'HRMS', 'Delete Payment', 7,
        (select id from permission where code = 'HRMS_EXPENCE_REPORT'), 'EMPLOYEE_EXPENSES');
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_EXPENSE_EDIT_PAYMENT', 'HRMS');
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_EXPENSE_DELETE_PAYMENT', 'HRMS');

