
insert into permission (code, context, name, sorder, parent, modulecode) values
('RECEIVE_PAYMENT_PDF',  'ACCOUNTING',   'PDF',       5,(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'), 'ACCOUNTING_MODULE'),
('RECEIVE_PAYMENT_VOID', 'ACCOUNTING',   'Void',      6,(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'), 'ACCOUNTING_MODULE'),
('PAY_INVOICE_SUMMARY',  'ACCOUNTING',   'Summary',   4,(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),        'ACCOUNTING_MODULE'),
('PAY_INVOICE_VOID',     'ACCOUNTING',   'Void',      5,(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),        'ACCOUNTING_MODULE'),
('PAY_INVOICE_PDF',      'ACCOUNTING',   'PDF',       6,(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),        'ACCOUNTING_MODULE');

insert into "anv".permission_context (permissioncode, contextcode) values ('RECEIVE_PAYMENT_PDF',   'ACCOUNTING'),
                                                                          ('RECEIVE_PAYMENT_VOID',  'ACCOUNTING'),
                                                                          ('PAY_INVOICE_SUMMARY',   'ACCOUNTING'),
                                                                          ('PAY_INVOICE_VOID',      'ACCOUNTING'),
                                                                          ('PAY_INVOICE_PDF',       'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, access, rolecode) values ('RECEIVE_PAYMENT_PDF',  'ALLOW',  'DR'),
                                                                           ('RECEIVE_PAYMENT_PDF',  'ALLOW',  'ACCOUNTANT'),
                                                                           ('RECEIVE_PAYMENT_VOID', 'ALLOW',  'DR'),
                                                                           ('RECEIVE_PAYMENT_VOID', 'ALLOW',  'ACCOUNTANT'),
                                                                           ('PAY_INVOICE_SUMMARY',  'ALLOW',  'DR'),
                                                                           ('PAY_INVOICE_SUMMARY',  'ALLOW',  'ACCOUNTANT'),
                                                                           ('PAY_INVOICE_VOID',     'ALLOW',  'DR'),
                                                                           ('PAY_INVOICE_VOID',     'ALLOW',  'ACCOUNTANT'),
                                                                           ('PAY_INVOICE_PDF',      'ALLOW',  'DR'),
                                                                           ('PAY_INVOICE_PDF',      'ALLOW',  'ACCOUNTANT');