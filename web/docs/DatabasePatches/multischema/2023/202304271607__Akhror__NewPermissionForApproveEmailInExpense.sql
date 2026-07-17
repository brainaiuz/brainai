insert into permission(code, context, name, sorder, parent, modulecode)
values ('EXPENSE_APPROVE_EMAIL', 'ACCOUNTING', 'Approve & Email', (select max(sorder)
                                                                   from permission
                                                                   where parent =
                                                                         (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST')),
        (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), 'EMPLOYEE_EXPENSES');
insert into "anv".permission_context(permissioncode, contextcode)
values ('EXPENSE_APPROVE_EMAIL', 'ACCOUNTING');