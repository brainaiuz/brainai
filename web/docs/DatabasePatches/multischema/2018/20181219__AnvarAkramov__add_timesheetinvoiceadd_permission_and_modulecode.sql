insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_TIMESHEET_INVOICE_ADD', 'ACCOUNTING', 'Timesheet Invoice Add', (select max(sorder) from permission where code='ACCOUNTING_ACCOUNTING_MENU')+1, false, (select id from permission where code='ACCOUNTING_ACCOUNTING_MENU'), false,  'TIMESHEET_INVOICES');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_TIMESHEET_INVOICE_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_TIMESHEET_INVOICE_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_TIMESHEET_INVOICE_ADD', 'ACCOUNTANT', 'ALLOW');