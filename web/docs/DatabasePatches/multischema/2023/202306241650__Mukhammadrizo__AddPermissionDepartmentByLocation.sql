delete from permission where code='HRMS_DEPARTMENT_LIST_BY_LOCATION';
insert into permission (code, context, name, parent, modulecode)
values ('HRMS_DEPARTMENT_LIST_BY_LOCATION', 'SETTINGS', 'Department List By Location',(select id from permission where code = 'HRMS_DEPARTMENT'),
        'HRMS_MODULE');

delete from "anv".permission_context where permissioncode = 'HRMS_DEPARTMENT_LIST_BY_LOCATION';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_DEPARTMENT_LIST_BY_LOCATION', 'SETTINGS');
