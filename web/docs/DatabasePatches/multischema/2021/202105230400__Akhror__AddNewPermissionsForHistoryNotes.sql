insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_SALES_QUOTE_HISTORY_NOTES', 'ACCOUNTING', false, 'History & Notes', 50,
        (select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), true, 'SALES_QUOTES');

insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_SALES_QUOTE_HISTORY_NOTES', 'ACCOUNTING');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_SALES_ORDER_HISTORY_NOTES', 'ACCOUNTING', false, 'History & Notes', 50,
        (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), true, 'SALES_ORDERS');

insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_SALES_ORDER_HISTORY_NOTES', 'ACCOUNTING');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_SALES_INVOICE_HISTORY_NOTES', 'ACCOUNTING', false, 'History & Notes', 50,
        (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), true, 'SALES_INVOICING');

insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_SALES_INVOICE_HISTORY_NOTES', 'ACCOUNTING');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_PURCHASE_ORDER_HISTORY_NOTES', 'ACCOUNTING', false, 'History & Notes', 50,
        (select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), true, 'PURCHASE_ORDERS');

insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_PURCHASE_ORDER_HISTORY_NOTES', 'ACCOUNTING');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_PURCHASE_INVOICE_HISTORY_NOTES', 'ACCOUNTING', false, 'History & Notes', 50,
        (select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), true, 'PURCHASE_INVOICING');

insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_PURCHASE_INVOICE_HISTORY_NOTES', 'ACCOUNTING');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_EXPENSE_REPORT_HISTORY_NOTES', 'ACCOUNTING', false, 'History & Notes', 50,
        (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), true, 'EXPENSE_REPORTING');

insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_EXPENSE_REPORT_HISTORY_NOTES', 'ACCOUNTING');