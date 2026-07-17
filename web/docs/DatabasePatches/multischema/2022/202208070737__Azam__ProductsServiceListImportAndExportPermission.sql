
--accounting export pdf / excel
delete from permission where code='ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT';
insert into permission (code, context, name, sorder, parent, modulecode) values ('ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT', 'ACCOUNTING', 'Export',
                                                                                 (SELECT max(sorder) + 1 from permission WHERE code = 'ACCOUNTING_PRODUCT_LIST'),
                                                                                 (select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), 'PRODUCTS_SERVICES');

--for crm
insert into permission (code, context, name, sorder, parent, modulecode) values ('ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT', 'CRM', 'Export',
                                                                                 (SELECT max(sorder) + 1 from permission WHERE code = 'CRM_PRODUCT_LIST'),
                                                                                 (select id from permission where code = 'CRM_PRODUCT_LIST'), 'PRODUCTS_SERVICES_CRM');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT', 'CRM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT', 'ALLOW', 'ADMIN'),
                                                                           ('ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT', 'ALLOW', 'ACCOUNTANT');


--accounting import
delete from permission where code='ACCOUNTING_PRODUCT_LIST_IMPORT';
insert into permission (code, context, name, sorder, parent, modulecode) values ('ACCOUNTING_PRODUCT_LIST_IMPORT', 'ACCOUNTING', 'Import',
                                                                                 (SELECT max(sorder) + 1 from permission WHERE code = 'ACCOUNTING_PRODUCT_LIST'),
                                                                                 (select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), 'PRODUCTS_SERVICES');

--for crm
insert into permission (code, context, name, sorder, parent, modulecode) values ('ACCOUNTING_PRODUCT_LIST_IMPORT', 'CRM', 'Export',
                                                                                 (SELECT max(sorder) + 1 from permission WHERE code = 'CRM_PRODUCT_LIST'),
                                                                                 (select id from permission where code = 'CRM_PRODUCT_LIST'), 'PRODUCTS_SERVICES_CRM');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PRODUCT_LIST_IMPORT';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_LIST_IMPORT', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_LIST_IMPORT', 'CRM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PRODUCT_LIST_IMPORT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PRODUCT_LIST_IMPORT', 'ALLOW', 'ADMIN'),
                                                                           ('ACCOUNTING_PRODUCT_LIST_IMPORT', 'ALLOW', 'ACCOUNTANT');



------------------------------------------------------------------------------------------------------------------------------------
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SO_AND_SQ_DRAFT' and contextcode = 'CRM';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SO_AND_SQ_DRAFT', 'CRM');