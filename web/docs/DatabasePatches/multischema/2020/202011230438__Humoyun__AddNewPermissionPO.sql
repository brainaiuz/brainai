
delete from permission where code='ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE';
insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'ACCOUNTING', 'Goods Received Notes', 17,(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),'PURCHASE_ORDERS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'CRM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'ALLOW', 'DR'),
                                                                              ('ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'ALLOW', 'ACCOUNTANT');




delete from permission where code='LOGISTICS_PURCHASE_ORDER_GOODS_RECEIVED_NOTE';
insert into permission (code, context, name, sorder, parent, modulecode) values
('LOGISTICS_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'ACCOUNTING', 'Goods Received Notes', 14,(select id from permission where code = 'LOGISTICS_PURCHASE_ORDER_LIST'),'PURCHASE_ORDERS');

delete from "anv".permission_context where permissioncode = 'LOGISTICS_PURCHASE_ORDER_GOODS_RECEIVED_NOTE';
insert into "anv".permission_context (permissioncode, contextcode) values ('LOGISTICS_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'ACCOUNTING'),
                                                                             ('LOGISTICS_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'CRM');

delete from "anv".rolepermission where permissioncode = 'LOGISTICS_PURCHASE_ORDER_GOODS_RECEIVED_NOTE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('LOGISTICS_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'ALLOW', 'DR'),
                                                                              ('LOGISTICS_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'ALLOW', 'ACCOUNTANT');




delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_LIST';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_ADD';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_EDIT';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_SUMMARY';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_DELETE';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_PDF';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_COPY';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_COPY_PI';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_COPY_SQ';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_IGNORE_MANAGER_APPROVAL';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_RECEIVE';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_FULL_LIST_ACCESS';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_FULL_EDIT_ACCESS';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_SAVE_AND_APPROVE_BUTTON';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_CAN_APPROVE_PURCHASE_ORDER';

insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PURCHASE_ORDER_LIST', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_ADD', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_EDIT', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_SUMMARY', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_DELETE', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_PDF', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_COPY', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_COPY_PI', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_SAVE_AND_APPROVE_BUTTON', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_COPY_SQ', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_IGNORE_MANAGER_APPROVAL', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_RECEIVE', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_CAN_APPROVE_PURCHASE_ORDER', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_FULL_LIST_ACCESS', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_FULL_EDIT_ACCESS', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_LIST', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_ADD', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_EDIT', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_SUMMARY', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_DELETE', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_PDF', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_COPY', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_COPY_PI', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_SAVE_AND_APPROVE_BUTTON', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_COPY_SQ', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_IGNORE_MANAGER_APPROVAL', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_RECEIVE', 'CRM'),
                                                                             ('ACCOUNTING_CAN_APPROVE_PURCHASE_ORDER', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_FULL_LIST_ACCESS', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_FULL_EDIT_ACCESS', 'CRM');

