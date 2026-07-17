delete from permission where code = 'ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS', 'ACCOUNTING', false, 'Full edit access', 50,
        (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), true, 'EXPENSE_REPORTING');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS';
insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS';
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS', 'ALLOW', 'DR');
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS', 'ALLOW', 'ACCOUNTANT');
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS', 'ALLOW', 'SALESMANAGER');


delete from permission where code = 'ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS', 'ACCOUNTING', false, 'Full delete access', 55,
        (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), true, 'EXPENSE_REPORTING');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS';
insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS';
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS', 'ALLOW', 'DR');
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS', 'ALLOW', 'ACCOUNTANT');
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS', 'ALLOW', 'SALESMANAGER');