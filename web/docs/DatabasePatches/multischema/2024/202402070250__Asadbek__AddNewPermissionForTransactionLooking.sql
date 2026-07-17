delete from permission where code='ACCOUNTING_TRANSACTION_LOOKING_LIST';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('ACCOUNTING_TRANSACTION_LOOKING_LIST', 'SETTINGS', 'Transaction Looking', 3,(select id from permission where code = 'SETTINGS_ACCOUNTING_SETTINGS'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_TRANSACTION_LOOKING_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_TRANSACTION_LOOKING_LIST', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_TRANSACTION_LOOKING_LIST';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_TRANSACTION_LOOKING_LIST', 'ALLOW', 'DR'),
                                                                             ('ACCOUNTING_TRANSACTION_LOOKING_LIST', 'ALLOW', 'ADMIN');



delete from permission where code='TRANSACTION_LOOKING_SALES';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('TRANSACTION_LOOKING_SALES', 'SETTINGS', 'Sales', 0,(select id from permission where code = 'ACCOUNTING_TRANSACTION_LOOKING_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'TRANSACTION_LOOKING_SALES';
insert into "anv".permission_context (permissioncode, contextcode) values ('TRANSACTION_LOOKING_SALES', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'TRANSACTION_LOOKING_SALES';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('TRANSACTION_LOOKING_SALES', 'ALLOW', 'DR'),
                                                                             ('TRANSACTION_LOOKING_SALES', 'ALLOW', 'ADMIN');



delete from permission where code='TRANSACTION_LOOKING_PURCHASES';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('TRANSACTION_LOOKING_PURCHASES', 'SETTINGS', 'Purchases', 1,(select id from permission where code = 'ACCOUNTING_TRANSACTION_LOOKING_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'TRANSACTION_LOOKING_PURCHASES';
insert into "anv".permission_context (permissioncode, contextcode) values ('TRANSACTION_LOOKING_PURCHASES', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'TRANSACTION_LOOKING_PURCHASES';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('TRANSACTION_LOOKING_PURCHASES', 'ALLOW', 'DR'),
                                                                             ('TRANSACTION_LOOKING_PURCHASES', 'ALLOW', 'ADMIN');



delete from permission where code='TRANSACTION_LOOKING_BANKING';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('TRANSACTION_LOOKING_BANKING', 'SETTINGS', 'Banking', 2,(select id from permission where code = 'ACCOUNTING_TRANSACTION_LOOKING_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'TRANSACTION_LOOKING_BANKING';
insert into "anv".permission_context (permissioncode, contextcode) values ('TRANSACTION_LOOKING_BANKING', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'TRANSACTION_LOOKING_BANKING';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('TRANSACTION_LOOKING_BANKING', 'ALLOW', 'DR'),
                                                                             ('TRANSACTION_LOOKING_BANKING', 'ALLOW', 'ADMIN');




delete from permission where code='TRANSACTION_LOOKING_EMPLOYEES';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('TRANSACTION_LOOKING_EMPLOYEES', 'SETTINGS', 'Employees', 3,(select id from permission where code = 'ACCOUNTING_TRANSACTION_LOOKING_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'TRANSACTION_LOOKING_EMPLOYEES';
insert into "anv".permission_context (permissioncode, contextcode) values ('TRANSACTION_LOOKING_EMPLOYEES', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'TRANSACTION_LOOKING_EMPLOYEES';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('TRANSACTION_LOOKING_EMPLOYEES', 'ALLOW', 'DR'),
                                                                             ('TRANSACTION_LOOKING_EMPLOYEES', 'ALLOW', 'ADMIN');



delete from permission where code='TRANSACTION_LOOKING_ATTENDANCE';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('TRANSACTION_LOOKING_ATTENDANCE', 'SETTINGS', 'Attendance tracking', 4,(select id from permission where code = 'ACCOUNTING_TRANSACTION_LOOKING_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'TRANSACTION_LOOKING_ATTENDANCE';
insert into "anv".permission_context (permissioncode, contextcode) values ('TRANSACTION_LOOKING_ATTENDANCE', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'TRANSACTION_LOOKING_ATTENDANCE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('TRANSACTION_LOOKING_ATTENDANCE', 'ALLOW', 'DR'),
                                                                             ('TRANSACTION_LOOKING_ATTENDANCE', 'ALLOW', 'ADMIN');


delete from permission where code='TRANSACTION_LOOKING_RECRUITMENT';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('TRANSACTION_LOOKING_RECRUITMENT', 'SETTINGS', 'Recruitment', 5,(select id from permission where code = 'ACCOUNTING_TRANSACTION_LOOKING_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'TRANSACTION_LOOKING_RECRUITMENT';
insert into "anv".permission_context (permissioncode, contextcode) values ('TRANSACTION_LOOKING_RECRUITMENT', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'TRANSACTION_LOOKING_RECRUITMENT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('TRANSACTION_LOOKING_RECRUITMENT', 'ALLOW', 'DR'),
                                                                             ('TRANSACTION_LOOKING_RECRUITMENT', 'ALLOW', 'ADMIN');


delete from permission where code='TRANSACTION_LOOKING_PAYSLIPS';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('TRANSACTION_LOOKING_PAYSLIPS', 'SETTINGS', 'Payslips', 6,(select id from permission where code = 'ACCOUNTING_TRANSACTION_LOOKING_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'TRANSACTION_LOOKING_PAYSLIPS';
insert into "anv".permission_context (permissioncode, contextcode) values ('TRANSACTION_LOOKING_PAYSLIPS', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'TRANSACTION_LOOKING_PAYSLIPS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('TRANSACTION_LOOKING_PAYSLIPS', 'ALLOW', 'DR'),
                                                                             ('TRANSACTION_LOOKING_PAYSLIPS', 'ALLOW', 'ADMIN');

delete from permission where code='TRANSACTION_LOOKING_CASH_ADVANCES';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('TRANSACTION_LOOKING_CASH_ADVANCES', 'SETTINGS', 'Cash Advance', 7,(select id from permission where code = 'ACCOUNTING_TRANSACTION_LOOKING_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'TRANSACTION_LOOKING_CASH_ADVANCES';
insert into "anv".permission_context (permissioncode, contextcode) values ('TRANSACTION_LOOKING_CASH_ADVANCES', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'TRANSACTION_LOOKING_CASH_ADVANCES';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('TRANSACTION_LOOKING_CASH_ADVANCES', 'ALLOW', 'DR'),
                                                                             ('TRANSACTION_LOOKING_CASH_ADVANCES', 'ALLOW', 'ADMIN');


delete from permission where code='TRANSACTION_LOOKING_ADDITIONAL_PAYMENTS';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('TRANSACTION_LOOKING_ADDITIONAL_PAYMENTS', 'SETTINGS', 'Additional Payments', 8,(select id from permission where code = 'ACCOUNTING_TRANSACTION_LOOKING_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'TRANSACTION_LOOKING_ADDITIONAL_PAYMENTS';
insert into "anv".permission_context (permissioncode, contextcode) values ('TRANSACTION_LOOKING_ADDITIONAL_PAYMENTS', 'SETTINGS');


delete from "anv".rolepermission where permissioncode = 'TRANSACTION_LOOKING_ADDITIONAL_PAYMENTS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('TRANSACTION_LOOKING_ADDITIONAL_PAYMENTS', 'ALLOW', 'DR'),
                                                                             ('TRANSACTION_LOOKING_ADDITIONAL_PAYMENTS', 'ALLOW', 'ADMIN');