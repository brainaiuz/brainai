
delete from permission where code='ACCOUNTING_PRODUCT_UPLOAD_FILES';
insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_PRODUCT_UPLOAD_FILES', 'ACCOUNTING', 'Upload Files', 11, (select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),'PRODUCTS_SERVICES');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PRODUCT_UPLOAD_FILES';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_UPLOAD_FILES', 'ACCOUNTING');


delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PRODUCT_UPLOAD_FILES';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PRODUCT_UPLOAD_FILES', 'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_PRODUCT_UPLOAD_FILES', 'ALLOW', 'SALESMANAGER'),
                                                                           ('ACCOUNTING_PRODUCT_UPLOAD_FILES', 'ALLOW', 'ADMIN'),
                                                                           ('ACCOUNTING_PRODUCT_UPLOAD_FILES', 'ALLOW', 'ACCOUNTANT');