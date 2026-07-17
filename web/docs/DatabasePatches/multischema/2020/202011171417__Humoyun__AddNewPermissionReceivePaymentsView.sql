
delete from permission where code='ACCOUNTING_RECEIVE_PAYMENT';
delete from permission where code='RECEIVE_PAYMENT_EDIT';
delete from permission where code='RECEIVE_PAYMENT_DELETE';
delete from permission where code='RECEIVE_PAYMENT_SUMMARY';


delete from permission where code='ACCOUNTING_RECEIVE_PAYMENT_LIST';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_RECEIVE_PAYMENT_LIST', 'ACCOUNTING', 'Receive Payments', 9,
        (select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'),'ACCOUNTING_MODULE');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_RECEIVE_PAYMENT_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_RECEIVE_PAYMENT_LIST', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_RECEIVE_PAYMENT_LIST';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_RECEIVE_PAYMENT_LIST', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_RECEIVE_PAYMENT_LIST', 'ALLOW', 'SALESMAN');


insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_RECEIVE_PAYMENT', 'ACCOUNTING', 'Add', 1, (select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),'ACCOUNTING_MODULE'),
       ('RECEIVE_PAYMENT_EDIT', 'ACCOUNTING', 'Edit', 2, (select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),'ACCOUNTING_MODULE'),
       ('RECEIVE_PAYMENT_DELETE', 'ACCOUNTING', 'Delete', 3, (select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),'ACCOUNTING_MODULE'),
       ('RECEIVE_PAYMENT_SUMMARY', 'ACCOUNTING', 'Summary', 4, (select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),'ACCOUNTING_MODULE');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_RECEIVE_PAYMENT';
delete from "anv".permission_context where permissioncode = 'RECEIVE_PAYMENT_EDIT';
delete from "anv".permission_context where permissioncode = 'RECEIVE_PAYMENT_DELETE';
delete from "anv".permission_context where permissioncode = 'RECEIVE_PAYMENT_SUMMARY';

insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_RECEIVE_PAYMENT', 'ACCOUNTING'),
                                                                          ('RECEIVE_PAYMENT_EDIT', 'ACCOUNTING'),
                                                                          ('RECEIVE_PAYMENT_DELETE', 'ACCOUNTING'),
                                                                          ('RECEIVE_PAYMENT_SUMMARY', 'ACCOUNTING');





