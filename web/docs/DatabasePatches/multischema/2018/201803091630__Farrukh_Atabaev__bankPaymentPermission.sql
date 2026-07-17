insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'ACCOUNTING', 'Cash Receipt Add', 1, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT', 'ACCOUNTING', 'Cash Receipt Edit', 2, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE', 'ACCOUNTING', 'Cash Receipt Delete', 3, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY', 'ACCOUNTING', 'Cash Receipt Summary', 4, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'), 'ACCOUNTING_MODULE');


insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY', 'ACCOUNTANT','ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY', 'ACCOUNTANT','ALLOW');

insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY', 'ACCOUNTING');


insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'ACCOUNTING', 'Cash Payment Add', 1, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT', 'ACCOUNTING', 'Cash Payment Edit', 2, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE', 'ACCOUNTING', 'Cash Payment Delete', 3, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY', 'ACCOUNTING', 'Cash Payment Summary', 4, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'), 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY', 'ACCOUNTANT','ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY', 'ACCOUNTANT','ALLOW');

insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY', 'ACCOUNTING');


insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'ACCOUNTING', 'Bank Receipts Add', 1, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_RECEIVE'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT', 'ACCOUNTING', 'Bank Receipts Edit', 2, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_RECEIVE'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE', 'ACCOUNTING', 'Bank Receipts Delete', 3, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_RECEIVE'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY', 'ACCOUNTING', 'Bank Receipts Summary', 4, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_RECEIVE'), 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY', 'ACCOUNTANT','ALLOW');

insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY', 'ACCOUNTING');


insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'ACCOUNTING', 'Bank Payments Add', 1, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_SPEND'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT', 'ACCOUNTING', 'Bank Payments Edit', 2, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_SPEND'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE', 'ACCOUNTING', 'Bank Payments Delete', 3, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_SPEND'), 'ACCOUNTING_MODULE');
insert into permission (code, context, name, sorder, parent, modulecode) values	('ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY', 'ACCOUNTING', 'Bank Payments Summary', 4, (select id from permission where code ='ACCOUNTING_BANK_ACCOUNT_SPEND'), 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY', 'ACCOUNTANT','ALLOW');

insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE', 'ACCOUNTING');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY', 'ACCOUNTING');