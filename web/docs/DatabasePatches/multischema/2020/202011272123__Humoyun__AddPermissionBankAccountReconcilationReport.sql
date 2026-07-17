
update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Add' where code='ACCOUNTING_BANK_ACCOUNT_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Bank Payments' where code='ACCOUNTING_BANK_ACCOUNT_SPEND';
update permission set  sorder=6, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Bank Receipts' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE';
update permission set  sorder=7, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Cash Receipt' where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT';
update permission set  sorder=8, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Cash Payment' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT';
update permission set  sorder=9, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Transfer Money' where code='ACCOUNTING_BANK_ACCOUNT_TRANSFER';
update permission set  sorder=10, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Import Transactions' where code='ACCOUNTING_BANK_ACCOUNT_TRANSACTION_IMPORT';
update permission set  sorder=11, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Account Transactions' where code='ACCOUNTING_BANK_ACCOUNT_TRANSACTIONS';
update permission set  sorder=12, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),name='Bank Statements' where code='ACCOUNTING_BANK_STATEMENT';


update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_SPEND'),name='Add' where code='ACCOUNTING_BANK_ACCOUNT_SPEND_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_SPEND'),name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_SPEND'),name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_SPEND'),name='Summary' where code='ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY';


update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE'),name='Add' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE'),name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE'),name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE'),name='Summary' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY';


update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),name='Add' where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),name='Summary' where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY';


update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),name='Add' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),name='Summary' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY';



delete from permission where code = 'ACCOUNTING_BANK_ACCOUNT_SUMMARY';
delete from permission where code = 'ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_BANK_ACCOUNT_SUMMARY', 'ACCOUNTING', 'Summary', 4, (select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'), 'BANK_ACCOUNTS'),
       ('ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT', 'ACCOUNTING', 'Reconcilation Report', 13, (select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'), 'BANK_ACCOUNTS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_SUMMARY';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT';
insert into "anv".permission_context (permissioncode, contextcode)
values ('ACCOUNTING_BANK_ACCOUNT_SUMMARY', 'ACCOUNTING'),
       ('ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_SUMMARY';
delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_BANK_ACCOUNT_SUMMARY', 'ALLOW', 'DR'),
       ('ACCOUNTING_BANK_ACCOUNT_SUMMARY', 'ALLOW', 'ACCOUNTANT'),
       ('ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT', 'ALLOW', 'DR'),
       ('ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT', 'ALLOW', 'ACCOUNTANT');