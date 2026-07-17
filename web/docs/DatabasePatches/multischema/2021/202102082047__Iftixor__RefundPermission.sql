

delete from permission where code='CUSTOMER_PREPAYMENT_REFUND_ADD';
delete from permission where code='CUSTOMER_PREPAYMENT_REFUND_VIEW';
delete from permission where code='CUSTOMER_PREPAYMENT_REFUND_DELETE';
insert into permission (code, context, name, sorder, parent, modulecode) values
('CUSTOMER_PREPAYMENT_REFUND_ADD', 'ACCOUNTING', 'Customer Refund Add', 2,(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),'ACCOUNTING_CUSTOMER_CENTER'),
('CUSTOMER_PREPAYMENT_REFUND_VIEW', 'ACCOUNTING', 'Customer Refund Summary', 3,(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),'ACCOUNTING_CUSTOMER_CENTER'),
('CUSTOMER_PREPAYMENT_REFUND_DELETE', 'ACCOUNTING', 'Customer Refund Delete', 4,(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),'ACCOUNTING_CUSTOMER_CENTER');

delete from "anv".permission_context where permissioncode = 'CUSTOMER_PREPAYMENT_REFUND_ADD';
delete from "anv".permission_context where permissioncode = 'CUSTOMER_PREPAYMENT_REFUND_VIEW';
delete from "anv".permission_context where permissioncode = 'CUSTOMER_PREPAYMENT_REFUND_DELETE';
insert into "anv".permission_context (permissioncode, contextcode) values
('CUSTOMER_PREPAYMENT_REFUND_ADD', 'ACCOUNTING'),
('CUSTOMER_PREPAYMENT_REFUND_DELETE', 'ACCOUNTING'),
('CUSTOMER_PREPAYMENT_REFUND_VIEW', 'ACCOUNTING');


delete from "anv".rolepermission where permissioncode = 'CUSTOMER_PREPAYMENT_REFUND_ADD';
delete from "anv".rolepermission where permissioncode = 'CUSTOMER_PREPAYMENT_REFUND_VIEW';
delete from "anv".rolepermission where permissioncode = 'CUSTOMER_PREPAYMENT_REFUND_DELETE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values
                                                                           ('CUSTOMER_PREPAYMENT_REFUND_ADD', 'ALLOW', 'DR'),
                                                                           ('CUSTOMER_PREPAYMENT_REFUND_ADD', 'ALLOW', 'ADMIN'),
                                                                           ('CUSTOMER_PREPAYMENT_REFUND_ADD', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('CUSTOMER_PREPAYMENT_REFUND_DELETE', 'ALLOW', 'DR'),
                                                                           ('CUSTOMER_PREPAYMENT_REFUND_DELETE', 'ALLOW', 'ADMIN'),
                                                                           ('CUSTOMER_PREPAYMENT_REFUND_DELETE', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('CUSTOMER_PREPAYMENT_REFUND_VIEW', 'ALLOW', 'DR'),
                                                                           ('CUSTOMER_PREPAYMENT_REFUND_VIEW', 'ALLOW', 'ADMIN'),
                                                                           ('CUSTOMER_PREPAYMENT_REFUND_VIEW', 'ALLOW', 'ACCOUNTANT');






delete from permission where code='SUPPLIER_PREPAYMENT_REFUND_ADD';
delete from permission where code='SUPPLIER_PREPAYMENT_REFUND_VIEW';
delete from permission where code='SUPPLIER_PREPAYMENT_REFUND_DELETE';
insert into permission (code, context, name, sorder, parent, modulecode) values
('SUPPLIER_PREPAYMENT_REFUND_ADD', 'ACCOUNTING', 'Supplier Refund Add', 2,(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),'ACCOUNTING_MODULE'),
('SUPPLIER_PREPAYMENT_REFUND_VIEW', 'ACCOUNTING', 'Supplier Refund Summary', 3,(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),'ACCOUNTING_MODULE'),
('SUPPLIER_PREPAYMENT_REFUND_DELETE', 'ACCOUNTING', 'Supplier Refund Delete', 4,(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'SUPPLIER_PREPAYMENT_REFUND_ADD';
delete from "anv".permission_context where permissioncode = 'SUPPLIER_PREPAYMENT_REFUND_VIEW';
delete from "anv".permission_context where permissioncode = 'SUPPLIER_PREPAYMENT_REFUND_DELETE';
insert into "anv".permission_context (permissioncode, contextcode) values
('SUPPLIER_PREPAYMENT_REFUND_ADD', 'ACCOUNTING'),
('SUPPLIER_PREPAYMENT_REFUND_DELETE', 'ACCOUNTING'),
('SUPPLIER_PREPAYMENT_REFUND_VIEW', 'ACCOUNTING');


delete from "anv".rolepermission where permissioncode = 'SUPPLIER_PREPAYMENT_REFUND_ADD';
delete from "anv".rolepermission where permissioncode = 'SUPPLIER_PREPAYMENT_REFUND_VIEW';
delete from "anv".rolepermission where permissioncode = 'SUPPLIER_PREPAYMENT_REFUND_DELETE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values
                                                                           ('SUPPLIER_PREPAYMENT_REFUND_ADD', 'ALLOW', 'DR'),
                                                                           ('SUPPLIER_PREPAYMENT_REFUND_ADD', 'ALLOW', 'ADMIN'),
                                                                           ('SUPPLIER_PREPAYMENT_REFUND_ADD', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('SUPPLIER_PREPAYMENT_REFUND_DELETE', 'ALLOW', 'DR'),
                                                                           ('SUPPLIER_PREPAYMENT_REFUND_DELETE', 'ALLOW', 'ADMIN'),
                                                                           ('SUPPLIER_PREPAYMENT_REFUND_DELETE', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('SUPPLIER_PREPAYMENT_REFUND_VIEW', 'ALLOW', 'DR'),
                                                                           ('SUPPLIER_PREPAYMENT_REFUND_VIEW', 'ALLOW', 'ADMIN'),
                                                                           ('SUPPLIER_PREPAYMENT_REFUND_VIEW', 'ALLOW', 'ACCOUNTANT');                                                                           

