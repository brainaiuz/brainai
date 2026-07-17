
update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Company Expense List' where code='ACCOUNTING_COMPANY_EXPENSE_LIST';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Add' where code='ACCOUNTING_EXPENSE_REPORT_ADD';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Edit' where code='ACCOUNTING_EXPENSE_REPORT_EDIT';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Delete' where code='ACCOUNTING_EXPENSE_REPORT_DELETE';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Approve' where code='ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Void' where code='ACCOUNTING_EXPENSE_REPORT_VOID';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Add/View Full Access' where code='EXPENSE_ADD_VIEW_FULL_ACCESS';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Add to Staff' where code='ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Full List Access' where code='ACCOUNTING_EXPENSE_FULL_LIST_ACCESS';
update permission set sorder=12, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Add Category' where code='ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY';
update permission set sorder=13, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Accountant Approval' where code='ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM_DOUBLE_APPROVE';
update permission set sorder=14, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Import from CSV' where code='SHOW_IMPORT_EXPENCE';
update permission set sorder=15, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='See Own' where code='EXPENSE_SEE_OWN';



delete from permission where code='ACCOUNTING_EXPENSE_REPORT_COPY';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_EXPENSE_REPORT_COPY', 'ACCOUNTING', 'Copy', 5,
        (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),'EXPENSE_REPORTING');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_EXPENSE_REPORT_COPY';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_EXPENSE_REPORT_COPY', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_EXPENSE_REPORT_COPY', 'PM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_EXPENSE_REPORT_COPY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_EXPENSE_REPORT_COPY', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_EXPENSE_REPORT_COPY', 'ALLOW', 'ACCOUNTANT');


delete from permission where code='ACCOUNTING_EXPENSE_REPORT_DRAFT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_EXPENSE_REPORT_DRAFT', 'ACCOUNTING', 'Draft', 7,
        (select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),'EXPENSE_REPORTING');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_EXPENSE_REPORT_DRAFT';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_EXPENSE_REPORT_DRAFT', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_EXPENSE_REPORT_DRAFT', 'PM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_EXPENSE_REPORT_DRAFT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_EXPENSE_REPORT_DRAFT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_EXPENSE_REPORT_DRAFT', 'ALLOW', 'ACCOUNTANT');

