
delete from permission where code='ACCOUNTING_CONVERSION_BALANCE';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACCOUNTING_CONVERSION_BALANCE',
                                                                            'SETTINGS',
                                                                            'Conversion Balance',
                                                                            5,
                                                                            (select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS'),
                                                                            'ACCOUNTING_MODULE'
                                                                           );

delete from "anv".rolepermission where permissioncode='ACCOUNTING_CONVERSION_BALANCE';
delete from "anv".permission_context where permissioncode='ACCOUNTING_CONVERSION_BALANCE';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_CONVERSION_BALANCE','SETTINGS');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('ACCOUNTING_CONVERSION_BALANCE','ALLOW','ADMIN');


ALTER TABLE "anv".conversionBalanceItem alter COLUMN debit type numeric(25,5);
ALTER TABLE "anv".conversionBalanceItem alter COLUMN credit type numeric(25,5);
ALTER TABLE "anv".conversionBalance alter COLUMN totalDebit type numeric(25,5);
ALTER TABLE "anv".conversionBalance alter COLUMN totalCredit type numeric(25,5);
