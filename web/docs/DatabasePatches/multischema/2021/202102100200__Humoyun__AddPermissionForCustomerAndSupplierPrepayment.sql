
--CUSTOMER PREPAYMENT
insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_PREPAYMENT_SUMMARY', 'ACCOUNTING', 'Summary', 2,(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_PREPAYMENT_EDIT',    'ACCOUNTING', 'Edit',    3,(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_PREPAYMENT_VOID',    'ACCOUNTING', 'Void',    4,(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_PREPAYMENT_DELETE',  'ACCOUNTING', 'Delete',  5,(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_PREPAYMENT_PDF',     'ACCOUNTING', 'PDF',     6,(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'), 'ACCOUNTING_MODULE');

insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PREPAYMENT_SUMMARY', 'ACCOUNTING'),
                                                                          ('ACCOUNTING_PREPAYMENT_EDIT',    'ACCOUNTING'),
                                                                          ('ACCOUNTING_PREPAYMENT_VOID',    'ACCOUNTING'),
                                                                          ('ACCOUNTING_PREPAYMENT_DELETE',  'ACCOUNTING'),
                                                                          ('ACCOUNTING_PREPAYMENT_PDF',     'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PREPAYMENT_SUMMARY', 'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_PREPAYMENT_SUMMARY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_PREPAYMENT_EDIT',    'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_PREPAYMENT_EDIT',    'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_PREPAYMENT_VOID',    'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_PREPAYMENT_VOID',    'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_PREPAYMENT_DELETE',  'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_PREPAYMENT_DELETE',  'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_PREPAYMENT_PDF',     'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_PREPAYMENT_PDF',     'ALLOW', 'ACCOUNTANT');




--LOGISTIC CUSTOMER PREPAYMENT
insert into permission (code, context, name, sorder, parent, modulecode) values
('LOGISTICS_PREPAYMENT_SUMMARY', 'LOGISTICS', 'Summary', 2,(select id from permission where code = 'LOGISTICS_PREPAYMENT_LIST'), 'LOGISTICS_MODULE'),
('LOGISTICS_PREPAYMENT_EDIT',    'LOGISTICS', 'Edit',    3,(select id from permission where code = 'LOGISTICS_PREPAYMENT_LIST'), 'LOGISTICS_MODULE'),
('LOGISTICS_PREPAYMENT_VOID',    'LOGISTICS', 'Void',    4,(select id from permission where code = 'LOGISTICS_PREPAYMENT_LIST'), 'LOGISTICS_MODULE'),
('LOGISTICS_PREPAYMENT_DELETE',  'LOGISTICS', 'Delete',  5,(select id from permission where code = 'LOGISTICS_PREPAYMENT_LIST'), 'LOGISTICS_MODULE'),
('LOGISTICS_PREPAYMENT_PDF',     'LOGISTICS', 'PDF',     6,(select id from permission where code = 'LOGISTICS_PREPAYMENT_LIST'), 'LOGISTICS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode) values ('LOGISTICS_PREPAYMENT_SUMMARY', 'LOGISTICS'),
                                                                          ('LOGISTICS_PREPAYMENT_EDIT',    'LOGISTICS'),
                                                                          ('LOGISTICS_PREPAYMENT_VOID',    'LOGISTICS'),
                                                                          ('LOGISTICS_PREPAYMENT_DELETE',  'LOGISTICS'),
                                                                          ('LOGISTICS_PREPAYMENT_PDF',     'LOGISTICS');

insert into "anv".rolepermission (permissioncode, access, rolecode) values ('LOGISTICS_PREPAYMENT_SUMMARY', 'ALLOW', 'DR'),
                                                                           ('LOGISTICS_PREPAYMENT_SUMMARY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_PREPAYMENT_EDIT',    'ALLOW', 'DR'),
                                                                           ('LOGISTICS_PREPAYMENT_EDIT',    'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_PREPAYMENT_VOID',    'ALLOW', 'DR'),
                                                                           ('LOGISTICS_PREPAYMENT_VOID',    'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_PREPAYMENT_DELETE',  'ALLOW', 'DR'),
                                                                           ('LOGISTICS_PREPAYMENT_DELETE',  'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_PREPAYMENT_PDF',     'ALLOW', 'DR'),
                                                                           ('LOGISTICS_PREPAYMENT_PDF',     'ALLOW', 'ACCOUNTANT');



--SUPPLIER PREPAYMENT
insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_SUPPLIER_CREDIT_SUMMARY', 'ACCOUNTING', 'Summary', 2,(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_SUPPLIER_CREDIT_EDIT',    'ACCOUNTING', 'Edit',    3,(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_SUPPLIER_CREDIT_VOID',    'ACCOUNTING', 'Void',    4,(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_SUPPLIER_CREDIT_DELETE',  'ACCOUNTING', 'Delete',  5,(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_SUPPLIER_CREDIT_PDF',     'ACCOUNTING', 'PDF',     6,(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'), 'ACCOUNTING_MODULE');

insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SUPPLIER_CREDIT_SUMMARY', 'ACCOUNTING'),
                                                                          ('ACCOUNTING_SUPPLIER_CREDIT_EDIT',    'ACCOUNTING'),
                                                                          ('ACCOUNTING_SUPPLIER_CREDIT_VOID',    'ACCOUNTING'),
                                                                          ('ACCOUNTING_SUPPLIER_CREDIT_DELETE',  'ACCOUNTING'),
                                                                          ('ACCOUNTING_SUPPLIER_CREDIT_PDF',     'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_SUPPLIER_CREDIT_SUMMARY', 'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_SUMMARY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_EDIT',    'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_EDIT',    'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_VOID',    'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_VOID',    'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_DELETE',  'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_DELETE',  'ALLOW', 'ACCOUNTANT'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_PDF',     'ALLOW', 'DR'),
                                                                           ('ACCOUNTING_SUPPLIER_CREDIT_PDF',     'ALLOW', 'ACCOUNTANT');




--LOGISTIC SUPPLIER PREPAYMENT
insert into permission (code, context, name, sorder, parent, modulecode) values
('LOGISTICS_SUPPLIER_CREDIT_SUMMARY', 'LOGISTICS', 'Summary', 2,(select id from permission where code = 'LOGISTICS_SUPPLIER_CREDIT_LIST'), 'LOGISTICS_MODULE'),
('LOGISTICS_SUPPLIER_CREDIT_EDIT',    'LOGISTICS', 'Edit',    3,(select id from permission where code = 'LOGISTICS_SUPPLIER_CREDIT_LIST'), 'LOGISTICS_MODULE'),
('LOGISTICS_SUPPLIER_CREDIT_VOID',    'LOGISTICS', 'Void',    4,(select id from permission where code = 'LOGISTICS_SUPPLIER_CREDIT_LIST'), 'LOGISTICS_MODULE'),
('LOGISTICS_SUPPLIER_CREDIT_DELETE',  'LOGISTICS', 'Delete',  5,(select id from permission where code = 'LOGISTICS_SUPPLIER_CREDIT_LIST'), 'LOGISTICS_MODULE'),
('LOGISTICS_SUPPLIER_CREDIT_PDF',     'LOGISTICS', 'PDF',     6,(select id from permission where code = 'LOGISTICS_SUPPLIER_CREDIT_LIST'), 'LOGISTICS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode) values ('LOGISTICS_SUPPLIER_CREDIT_SUMMARY', 'LOGISTICS'),
                                                                          ('LOGISTICS_SUPPLIER_CREDIT_EDIT',    'LOGISTICS'),
                                                                          ('LOGISTICS_SUPPLIER_CREDIT_VOID',    'LOGISTICS'),
                                                                          ('LOGISTICS_SUPPLIER_CREDIT_DELETE',  'LOGISTICS'),
                                                                          ('LOGISTICS_SUPPLIER_CREDIT_PDF',     'LOGISTICS');

insert into "anv".rolepermission (permissioncode, access, rolecode) values ('LOGISTICS_SUPPLIER_CREDIT_SUMMARY', 'ALLOW', 'DR'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_SUMMARY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_EDIT',    'ALLOW', 'DR'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_EDIT',    'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_VOID',    'ALLOW', 'DR'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_VOID',    'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_DELETE',  'ALLOW', 'DR'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_DELETE',  'ALLOW', 'ACCOUNTANT'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_PDF',     'ALLOW', 'DR'),
                                                                           ('LOGISTICS_SUPPLIER_CREDIT_PDF',     'ALLOW', 'ACCOUNTANT');