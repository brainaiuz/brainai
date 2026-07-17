
delete from permission where code='ACCOUNTING_MANUAL_JOURNAL_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACCOUNTING_MANUAL_JOURNAL_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            6,
                                                                            (select id from permission where code='ACCOUNTING_MANUAL_JOURNAL_LIST'),
                                                                            'ACCOUNTING_MODULE'
                                                                           );

delete from "anv".rolepermission where permissioncode='ACCOUNTING_MANUAL_JOURNAL_SEE_OWN';
delete from "anv".permission_context where permissioncode='ACCOUNTING_MANUAL_JOURNAL_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_MANUAL_JOURNAL_SEE_OWN','ACCOUNTING');
insert into "anv".rolepermission (permissioncode,access,rolecode) values
                                                                         ('ACCOUNTING_MANUAL_JOURNAL_SEE_OWN','ALLOW','ADMIN'),
                                                                         ('ACCOUNTING_MANUAL_JOURNAL_SEE_OWN','ALLOW','ACCOUNTANT'),
                                                                         ('ACCOUNTING_MANUAL_JOURNAL_SEE_OWN','ALLOW','DR');
