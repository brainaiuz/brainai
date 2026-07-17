
delete from permission where code='HIDE_PRODUCT_PRICE';
delete from "anv".permission_context where permissioncode = 'HIDE_PRODUCT_PRICE';
delete from "anv".rolepermission where permissioncode = 'HIDE_PRODUCT_PRICE';


delete from permission where code='ACCOUNTING_PRODUCT_SELLING';
insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_PRODUCT_SELLING', 'ACCOUNTING', 'Selling Price', 11, (select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),'PRODUCTS_SERVICES');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PRODUCT_SELLING';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_SELLING', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_SELLING', 'CRM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PRODUCT_SELLING';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PRODUCT_SELLING', 'ALLOW', 'ADMIN');


delete from permission where code='ACCOUNTING_PRODUCT_AVARAGE_COST';
insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_PRODUCT_AVARAGE_COST', 'ACCOUNTING', 'Avarage Cost', 12, (select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),'PRODUCTS_SERVICES');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PRODUCT_AVARAGE_COST';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_AVARAGE_COST', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_AVARAGE_COST', 'CRM');


delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PRODUCT_AVARAGE_COST';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PRODUCT_AVARAGE_COST', 'ALLOW', 'ADMIN');