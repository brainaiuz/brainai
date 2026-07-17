
--CUSTOMER PREPAYMENT
delete from permission where code='ACCOUNTING_PREPAYMENT_COPY';
insert into permission (code, context, name, sorder, parent, modulecode) values ('ACCOUNTING_PREPAYMENT_COPY', 'ACCOUNTING', 'Copy',
                                                                                 (SELECT max(sorder) + 1 from permission WHERE code = 'ACCOUNTING_PREPAYMENT_LIST'),
                                                                                 (select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'), 'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PREPAYMENT_COPY';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PREPAYMENT_COPY', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PREPAYMENT_COPY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PREPAYMENT_COPY', 'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_PREPAYMENT_COPY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_PREPAYMENT_COPY', 'ALLOW', 'ADMIN');


--LOGISTIC CUSTOMER PREPAYMENT
delete from permission where code='LOGISTICS_PREPAYMENT_COPY';
insert into permission (code, context, name, sorder, parent, modulecode) values ('LOGISTICS_PREPAYMENT_COPY', 'LOGISTICS', 'Copy',
                                                                                 (select  max(sorder) + 1 from permission where code = 'LOGISTICS_PREPAYMENT_LIST'),
                                                                                 (select id from permission where code = 'LOGISTICS_PREPAYMENT_LIST'), 'LOGISTICS_MODULE');

delete from "anv".permission_context where permissioncode = 'LOGISTICS_PREPAYMENT_COPY';
insert into "anv".permission_context (permissioncode, contextcode) values ('LOGISTICS_PREPAYMENT_COPY', 'LOGISTICS');

delete from "anv".rolepermission where permissioncode = 'LOGISTICS_PREPAYMENT_COPY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('LOGISTICS_PREPAYMENT_COPY', 'ALLOW', 'DR'),
                                                                           ('LOGISTICS_PREPAYMENT_COPY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_PREPAYMENT_COPY', 'ALLOW', 'ADMIN');


--SUPPLIER PREPAYMENT
delete from permission where code='ACCOUNTING_SUPPLIER_CREDIT_COPY';
insert into permission (code, context, name, sorder, parent, modulecode) values ('ACCOUNTING_SUPPLIER_CREDIT_COPY', 'ACCOUNTING', 'Copy',
                                                                                 (select max(sorder) + 1 from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
                                                                                 (select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'), 'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SUPPLIER_CREDIT_COPY';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SUPPLIER_CREDIT_COPY', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_SUPPLIER_CREDIT_COPY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_SUPPLIER_CREDIT_COPY', 'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_COPY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_COPY', 'ALLOW', 'ADMIN');


--LOGISTIC SUPPLIER PREPAYMENT
delete from permission where code='LOGISTICS_SUPPLIER_CREDIT_COPY';
insert into permission (code, context, name, sorder, parent, modulecode) values ('LOGISTICS_SUPPLIER_CREDIT_COPY', 'LOGISTICS', 'Copy',
                                                                                 (select max(sorder) + 1 from permission where code = 'LOGISTICS_SUPPLIER_CREDIT_LIST'),
                                                                                 (select id from permission where code = 'LOGISTICS_SUPPLIER_CREDIT_LIST'), 'LOGISTICS_MODULE');

delete from "anv".permission_context where permissioncode = 'LOGISTICS_SUPPLIER_CREDIT_COPY';
insert into "anv".permission_context (permissioncode, contextcode) values ('LOGISTICS_SUPPLIER_CREDIT_COPY', 'LOGISTICS');

delete from "anv".rolepermission where permissioncode = 'LOGISTICS_SUPPLIER_CREDIT_COPY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('LOGISTICS_SUPPLIER_CREDIT_COPY', 'ALLOW', 'DR'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_COPY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_COPY', 'ALLOW', 'ADMIN');