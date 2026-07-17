insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_SALES_QUOTE_LINKS', 'ACCOUNTING', false, 'Links', 45,
        (select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), true, 'SALES_QUOTES');

insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_SALES_QUOTE_LINKS', 'ACCOUNTING');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_SALES_ORDER_LINKS', 'ACCOUNTING', false, 'Links', 45,
        (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), true, 'SALES_ORDERS');

insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_SALES_ORDER_LINKS', 'ACCOUNTING');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_PURCHASE_ORDER_LINKS', 'ACCOUNTING', false, 'Links', 45,
        (select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), true, 'PURCHASE_ORDERS');

insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_PURCHASE_ORDER_LINKS', 'ACCOUNTING');