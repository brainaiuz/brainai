update permission
set sorder=1,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Add'
where code = 'HRMS_ADD_NEW_EXPENSE_CLAIM';
update permission
set sorder=2,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Edit'
where code = 'HRMS_EXPENSE_REPORT_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Delete'
where code = 'HRMS_EXPENCE_REPORT_REMOVE';
update permission
set sorder=4,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Summary'
where code = 'HRMS_VIEW_EXPENSE_CLAIM';
update permission
set sorder=5,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Add/Summary Full Access'
where code = 'HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS';
update permission
set sorder=6,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='See All'
where code = 'HRMS_EXPENSES_SEE_ALL';
update permission
set sorder=7,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Approve'
where code = 'HRMS_CAN_APPROVE_EXPENSE_CLAIM';
update permission
set sorder=9,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Void'
where code = 'HRMS_EXPENSE_REPORT_VOID';
update permission
set sorder=10,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Add Payment'
where code = 'HRMS_EXPENSE_ADD_PAYMENT';
update permission
set sorder=11,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Edit Payment'
where code = 'HRMS_EXPENSE_EDIT_PAYMENT';
update permission
set sorder=12,
    parent=(select id from permission where code = 'HRMS_EXPENCE_REPORT'),
    name='Delete Payment'
where code = 'HRMS_EXPENSE_DELETE_PAYMENT';



delete
from permission
where code = 'HRMS_EXPENSE_REPORT_DRAFT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_EXPENSE_REPORT_DRAFT', 'HRMS', 'Draft', 8,
        (select id from permission where code = 'HRMS_EXPENCE_REPORT'), 'EMPLOYEE_EXPENSES');

delete
from "anv".permission_context
where permissioncode = 'HRMS_EXPENSE_REPORT_DRAFT';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_EXPENSE_REPORT_DRAFT', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_EXPENSE_REPORT_DRAFT';

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_EXPENSE_REPORT_DRAFT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_EXPENSE_REPORT_DRAFT', 'ALLOW', 'ESS_USER');