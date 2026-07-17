
---LIST
DELETE from permission WHERE code='ACCOUNTING_EXPENSE_REPORT_LIST';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_LIST';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_LIST';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'ACCOUNTING', 'Expense List', (select max(sorder) from permission where code='ACCOUNTING_ACCOUNTING_MENU')+1, false, (select id from permission where code='ACCOUNTING_ACCOUNTING_MENU'), false,  'ACCOUNTING_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'ACCOUNTANT', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_LIST', 'ACCOUNTANT', 'ALLOW');

---ADD
DELETE from permission WHERE code='ACCOUNTING_EXPENSE_REPORT_ADD';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_ADD';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_ADD';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'ACCOUNTING', 'Expense Add', (select max(sorder) from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST')+1, false, (select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST'), false,  'ACCOUNTING_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD', 'ACCOUNTANT', 'ALLOW');

---EDIT
DELETE from permission WHERE code='ACCOUNTING_EXPENSE_REPORT_EDIT';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_EDIT';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_EDIT';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'ACCOUNTING', 'Expense Edit', (select max(sorder) from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST')+1, false, (select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST'), false,  'ACCOUNTING_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'ACCOUNTANT', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_EDIT', 'ACCOUNTANT', 'ALLOW');

---DELETE
DELETE from permission WHERE code='ACCOUNTING_EXPENSE_REPORT_DELETE';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_DELETE';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_DELETE';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_EXPENSE_REPORT_DELETE', 'ACCOUNTING', 'Expense Delete', (select max(sorder) from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST')+1, false, (select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST'), false,  'ACCOUNTING_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_DELETE', 'ACCOUNTANT', 'ALLOW');

---VOID
DELETE from permission WHERE code='ACCOUNTING_EXPENSE_REPORT_VOID';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_VOID';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_VOID';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'ACCOUNTING', 'Expense Void', (select max(sorder) from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST')+1, false, (select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST'), false,  'ACCOUNTING_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'ACCOUNTANT', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_VOID', 'ACCOUNTANT', 'ALLOW');

---ADD TO CATEGORY
DELETE from permission WHERE code='ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'ACCOUNTING', 'Add New Category (chart of accounts)', '6', 'false', (select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY', 'ACCOUNTANT', 'ALLOW');

---ADD TO STAFF
DELETE from permission WHERE code='ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'ACCOUNTING', 'Expense Add To Staff', (select max(sorder) from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST')+1, 'false', (select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF', 'ACCOUNTANT', 'ALLOW');

---LIST FULL ACCESS
DELETE from permission WHERE code='ACCOUNTING_EXPENSE_FULL_LIST_ACCESS';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_FULL_LIST_ACCESS';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_EXPENSE_FULL_LIST_ACCESS';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'ACCOUNTING', 'Expense List Full Access', (select max(sorder) from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST')+1, false, (select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST'), false,  'ACCOUNTING_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'ACCOUNTANT', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_EXPENSE_FULL_LIST_ACCESS', 'ACCOUNTANT', 'ALLOW');


-----------------------------------------------------COMPANY EXPENSE -------------------------------------------------------
DELETE from permission WHERE code='ACCOUNTING_COMPANY_EXPENSE_LIST';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_LIST';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_LIST';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
 values('ACCOUNTING_COMPANY_EXPENSE_LIST', 'ACCOUNTING', 'Company Expense List', (select sorder from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST')+1 , 'false', (select id from permission where code='ACCOUNTING_ACCOUNTING_MENU'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST', 'ACCOUNTANT', 'ALLOW');



DELETE from permission WHERE code='ACCOUNTING_COMPANY_EXPENSE_ADD';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_ADD';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_ADD';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ACCOUNTING', 'Company Expense Add',(select max(sorder) from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST')+1 , 'false', (select id from permission where code='ACCOUNTING_COMPANY_EXPENSE_REPORT_LIST'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ACCOUNTANT', 'ALLOW');


DELETE from permission WHERE code='ACCOUNTING_COMPANY_EXPENSE_EDIT';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_EDIT';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_EDIT';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_COMPANY_EXPENSE_EDIT', 'ACCOUNTING', 'Company Expense Edit',(select max(sorder) from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST')+1 , 'false', (select id from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_EDIT', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_EDIT', 'ACCOUNTANT', 'ALLOW');


DELETE from permission WHERE code='ACCOUNTING_COMPANY_EXPENSE_DELETE';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_DELETE';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_DELETE';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_COMPANY_EXPENSE_DELETE', 'ACCOUNTING', 'Company Expense Delete',(select max(sorder) from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST')+1 , 'false', (select id from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_DELETE', 'ACCOUNTANT', 'ALLOW');

DELETE from permission WHERE code='ACCOUNTING_COMPANY_EXPENSE_VOID';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_VOID';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_VOID';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_COMPANY_EXPENSE_VOID', 'ACCOUNTING', 'Company Expense Void',(select max(sorder) from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST')+1 , 'false', (select id from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_VOID', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_VOID', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_VOID', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_VOID', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_VOID', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_VOID', 'ACCOUNTANT', 'ALLOW');


DELETE from permission WHERE code='ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS', 'ACCOUNTING', 'Company Expense List Full Access',(select max(sorder) from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST')+1 , 'false', (select id from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS', 'ACCOUNTANT', 'ALLOW');



DELETE from permission WHERE code='ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'ACCOUNTING', 'Company Expense Approve Access',(select max(sorder) from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST')+1 , 'false', (select id from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM', 'ACCOUNTANT', 'ALLOW');