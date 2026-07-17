
delete from permission where code='ACCOUNTING_PRODUCT_HISTORY_LIST';
insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_PRODUCT_HISTORY_LIST', 'ACCOUNTING', 'History List', 11, (select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),'PRODUCTS_SERVICES');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PRODUCT_HISTORY_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_HISTORY_LIST', 'ACCOUNTING');


delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PRODUCT_HISTORY_LIST';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PRODUCT_HISTORY_LIST', 'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_PRODUCT_HISTORY_LIST', 'ALLOW', 'ADMIN'),
                                                                           ('ACCOUNTING_PRODUCT_HISTORY_LIST', 'ALLOW', 'ACCOUNTANT');

