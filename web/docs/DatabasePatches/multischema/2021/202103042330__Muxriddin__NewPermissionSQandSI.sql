
insert into permission (code, context, name, sorder, parent, modulecode) values
('SALES_QUOTE_CLIENT_APPROVE',          'ACCOUNTING',   'Client Approve',  14, (select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),      'SALES_QUOTES'),
('ACCOUNTING_PURCHASE_INVOICE_APPROVE', 'ACCOUNTING',   'Approve Invoice', 14, (select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), 'PURCHASE_INVOICING');

insert into "anv".permission_context (permissioncode, contextcode) values ('SALES_QUOTE_CLIENT_APPROVE',           'ACCOUNTING'),
                                                                          ('ACCOUNTING_PURCHASE_INVOICE_APPROVE',  'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, access, rolecode) values ('SALES_QUOTE_CLIENT_APPROVE',          'ALLOW',  'DR'),
                                                                           ('SALES_QUOTE_CLIENT_APPROVE',          'ALLOW',  'ACCOUNTANT'),
                                                                           ('ACCOUNTING_PURCHASE_INVOICE_APPROVE', 'ALLOW',  'DR'),
                                                                           ('ACCOUNTING_PURCHASE_INVOICE_APPROVE', 'ALLOW',  'ACCOUNTANT');