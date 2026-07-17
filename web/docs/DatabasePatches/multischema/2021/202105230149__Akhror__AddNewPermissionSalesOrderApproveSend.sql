insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('SALES_ORDER_APPROVE_EMAIL_SEND', 'ACCOUNTING', false, 'Approve & Email', 40,
        (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), true, 'SALES_ORDERS');

insert into "anv".permission_context(permissioncode, contextcode)
values ('SALES_ORDER_APPROVE_EMAIL_SEND', 'ACCOUNTING');