


insert into permission(code, context, name, sorder, parent, modulecode) values ('ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS','ACCOUNTING','Full List Access', 8 ,(select id from permission where code='ACCOUNTING_PURCHASE_INVOICE_LIST'),'PURCHASE_INVOICING');

insert into "0".rolepermission(permissioncode,access,rolecode) values ('ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS','ALLOW','ADMIN');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS','ALLOW','DR');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS','ALLOW','ACCOUNTANT');

delete from "0".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS' and contextcode='ACCOUNTING';
insert into "0".permission_context(permissioncode,contextcode) values ('ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS','ACCOUNTING');


insert into "anv".rolepermission(permissioncode,access,rolecode) values ('ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS','ALLOW','ACCOUNTANT');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS' and contextcode='ACCOUNTING';
insert into "anv".permission_context(permissioncode,contextcode) values ('ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS','ACCOUNTING');

update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Full Edit Access' where code='ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Add Credit Note' where code='ACCOUNTING_PURCHASE_CREDIT_NOTE_ADD';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Edit Credit Note' where code='ACCOUNTING_PURCHASE_CREDIT_NOTE_EDIT';
update permission set sorder=12, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Credit Note Full Edit Access' where code='ACCOUNTING_PURCHASE_CREDIT_NOTE_FULL_EDIT_ACCESS';

