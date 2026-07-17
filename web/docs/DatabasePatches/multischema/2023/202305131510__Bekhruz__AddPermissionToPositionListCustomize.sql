delete from permission where code='POSITION_CUSTOMIZE_LIST';
insert into permission (code, context, name, parent, modulecode)
values ('POSITION_CUSTOMIZE_LIST', 'SETTINGS', 'Customize Position List', (select id from permission where code='HRMS_POSITION'), 'HRMS_MODULE');

delete from "anv".permission_context where permissioncode = 'POSITION_CUSTOMIZE_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('POSITION_CUSTOMIZE_LIST', 'SETTINGS');
