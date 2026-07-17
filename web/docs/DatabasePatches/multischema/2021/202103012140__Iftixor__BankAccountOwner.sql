
delete from permission where code='BANKACCOUNT_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('BANKACCOUNT_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            5,
                                                                            (select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),
                                                                            'BANK_ACCOUNTS'
                                                                           );

delete from "anv".rolepermission where permissioncode='BANKACCOUNT_SEE_OWN';
delete from "anv".permission_context where permissioncode='BANKACCOUNT_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('BANKACCOUNT_SEE_OWN','ACCOUNTING');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('BANKACCOUNT_SEE_OWN','ALLOW','ADMIN');

delete from permission where code='ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS',
                                                                            'ACCOUNTING',
                                                                            'Full List Access',
                                                                            6,
                                                                            (select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),
                                                                            'BANK_ACCOUNTS'
                                                                           );

delete from "anv".rolepermission where permissioncode='ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS';
delete from "anv".permission_context where permissioncode='ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS','ACCOUNTING');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS','ALLOW','ADMIN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS','ALLOW','DR');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS','ALLOW','ACCOUNTANT');


update permission set sorder= 7 where code='ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT';
update permission set sorder= 8 where code='ACCOUNTING_BANK_ACCOUNT_SPEND';
update permission set sorder= 9 where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE';
update permission set sorder= 10 where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT';
update permission set sorder= 11 where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT';
update permission set sorder= 12 where code='ACCOUNTING_BANK_ACCOUNT_TRANSFER';
update permission set sorder= 13 where code='ACCOUNTING_BANK_ACCOUNT_TRANSACTION_IMPORT';
update permission set sorder= 14 where code='ACCOUNTING_BANK_ACCOUNT_TRANSACTIONS';
update permission set sorder= 15 where code='ACCOUNTING_BANK_STATEMENT';


delete from "anv".modelfield where form_id = 'BANK_ACCOUNT_FORM' and field_id='OWNER';

insert into "anv".modelfield(form_id, fsection, section, nolabelfor, fieldstyle, columntype, fieldsetstyle, mandatory, widget, forder, field_id) values
('BANK_ACCOUNT_FORM',	  'ACCOUNT_INFORMATION',      'ACCOUNT_INFORMATION', '', 'field', 'COL_1', 'slideDown-content group labelLine',false, 'TextBox',      7,	       'OWNER');
