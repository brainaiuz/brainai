
delete from permission where code='ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS',
                                                                            'ACCOUNTING',
                                                                            'See All',
                                                                            7,
                                                                            (select id from permission where code='ACCOUNTING_RECEIVE_PAYMENT_LIST'),
                                                                            'ACCOUNTING_MODULE'
                                                                           );

delete from "anv".rolepermission where permissioncode='ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS';
delete from "anv".permission_context where permissioncode='ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS','ACCOUNTING');
insert into "anv".rolepermission (permissioncode,access,rolecode) values
                                                                         ('ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS','ALLOW','ADMIN'),
                                                                         ('ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS','ALLOW','ACCOUNTANT'),
                                                                         ('ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS','ALLOW','DR');

delete from permission where code='ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            8,
                                                                            (select id from permission where code='ACCOUNTING_RECEIVE_PAYMENT_LIST'),
                                                                            'ACCOUNTING_MODULE'
                                                                           );

delete from "anv".rolepermission where permissioncode='ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN';
delete from "anv".permission_context where permissioncode='ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN','ACCOUNTING');
insert into "anv".rolepermission (permissioncode,access,rolecode) values
                                                                         ('ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN','ALLOW','ADMIN'),
                                                                         ('ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN','ALLOW','ACCOUNTANT'),
                                                                         ('ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN','ALLOW','DR');




delete from permission where code='ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS',
                                                                            'ACCOUNTING',
                                                                            'See All',
                                                                            7,
                                                                            (select id from permission where code='ACCOUNTING_PAY_BILL_LIST'),
                                                                            'ACCOUNTING_MODULE'
                                                                           );

delete from "anv".rolepermission where permissioncode='ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS';
delete from "anv".permission_context where permissioncode='ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS','ACCOUNTING');
insert into "anv".rolepermission (permissioncode,access,rolecode) values
('ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS','ALLOW','ADMIN'),
('ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS','ALLOW','ACCOUNTANT'),
('ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS','ALLOW','DR');

delete from permission where code='ACCOUNTING_PAY_BILL_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('ACCOUNTING_PAY_BILL_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            8,
                                                                            (select id from permission where code='ACCOUNTING_PAY_BILL_LIST'),
                                                                            'ACCOUNTING_MODULE'
                                                                           );

delete from "anv".rolepermission where permissioncode='ACCOUNTING_PAY_BILL_SEE_OWN';
delete from "anv".permission_context where permissioncode='ACCOUNTING_PAY_BILL_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('ACCOUNTING_PAY_BILL_SEE_OWN','ACCOUNTING');
insert into "anv".rolepermission (permissioncode,access,rolecode) values
('ACCOUNTING_PAY_BILL_SEE_OWN','ALLOW','ADMIN'),
('ACCOUNTING_PAY_BILL_SEE_OWN','ALLOW','ACCOUNTANT'),
('ACCOUNTING_PAY_BILL_SEE_OWN','ALLOW','DR');