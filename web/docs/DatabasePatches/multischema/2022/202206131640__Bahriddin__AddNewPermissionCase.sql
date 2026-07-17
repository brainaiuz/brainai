delete
from permission
where code = 'CRM_CASE_QUICK_ADD';

insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_CASE_QUICK_ADD', 'CRM', 'Case Quick Add', 15, (select id from permission where code = 'CRM_CASES_LIST'), 'CASE_MANAGEMENT');



delete
from "anv".permission_context
where permissioncode = 'CRM_CASE_QUICK_ADD';
insert into "anv".permission_context (permissioncode, contextcode)
values ('CRM_CASE_QUICK_ADD', 'CRM');

delete
from "anv".rolepermission
where permissioncode = 'CRM_CASE_QUICK_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('CRM_CASE_QUICK_ADD', 'ALLOW', 'ADMIN');