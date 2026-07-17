
delete from permission where code='BANKACCOUNT_SEE_OWN';
delete from "anv".rolepermission where permissioncode='BANKACCOUNT_SEE_OWN';
delete from "anv".permission_context where permissioncode='BANKACCOUNT_SEE_OWN';

delete from permission where code='ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS';
delete from "anv".rolepermission where permissioncode='ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS';
delete from "anv".permission_context where permissioncode='ACCOUNTING_BANKACCOUNT_FULL_LIST_ACCESS';


delete from permission where code='ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE',
                                                                            'ACCOUNTING',
                                                                            'Assignee List Value',
                                                                            5,
                                                                            (select id from permission where code='ACCOUNTING_BANK_ACCOUNT_LIST'),
                                                                            'BANK_ACCOUNTS'
                                                                           );

delete from "anv".rolepermission where permissioncode='ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE';
delete from "anv".permission_context where permissioncode='ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE','ACCOUNTING');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE','ALLOW','ADMIN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE','ALLOW','DR');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE','ALLOW','ACCOUNTANT');
