delete from permission where code='DEPARTMENT_CUSTOMIZE_LIST';
insert into permission (code, context, name, parent, modulecode)
values ('DEPARTMENT_CUSTOMIZE_LIST', 'SETTINGS', 'Customize List', (select id from permission where code='HRMS_DEPARTMENT'), 'HRMS_MODULE');

delete from "anv".permission_context where permissioncode = 'DEPARTMENT_CUSTOMIZE_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('DEPARTMENT_CUSTOMIZE_LIST', 'SETTINGS');


update permission set name = 'Customize List' where code = 'POSITION_CUSTOMIZE_LIST';
