

delete
from permission
where code = 'ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN', 'ACCOUNTING', 'Mark As Open', 17, (select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), 'PURCHASE_ORDERS');

delete
from "anv".permission_context
where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN';
insert into "anv".permission_context (permissioncode, contextcode)
values ('ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN', 'ACCOUNTING'),
       ('ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN', 'CRM');

delete
from "anv".rolepermission
where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN', 'ALLOW', 'DR'),
       ('ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN', 'ALLOW', 'ACCOUNTANT'),
       ('ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN', 'ALLOW', 'PM');



delete
from permission
where code = 'LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN', 'ACCOUNTING', 'Mark As Open', 14, (select id from permission where code = 'LOGISTICS_PURCHASE_ORDER_LIST'), 'LOGISTICS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN';
insert into "anv".permission_context (permissioncode, contextcode)
values ('LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN', 'ACCOUNTING'),
       ('LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN', 'CRM');

delete
from "anv".rolepermission
where permissioncode = 'LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN', 'ALLOW', 'DR'),
       ('LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN', 'ALLOW', 'ACCOUNTANT'),
       ('ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN', 'ALLOW', 'PM');