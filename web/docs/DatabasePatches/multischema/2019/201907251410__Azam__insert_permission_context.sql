delete from "0".permission_context where permissioncode = 'HRMS_ADD_NEW_LOCATION' and contextcode='SETTINGS';
insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_ADD_NEW_LOCATION', 'SETTINGS');

delete from "0".permission_context where permissioncode = 'HRMS_REMOVE_LOCATION' and contextcode='SETTINGS';
insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_REMOVE_LOCATION', 'SETTINGS');

delete from "0".permission_context where permissioncode = 'HRMS_EDIT_LOCATION' and contextcode='SETTINGS';
insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_EDIT_LOCATION', 'SETTINGS');

delete from "anv".permission_context where permissioncode = 'HRMS_ADD_NEW_LOCATION' and contextcode='SETTINGS';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_ADD_NEW_LOCATION', 'SETTINGS');

delete from "anv".permission_context where permissioncode = 'HRMS_EDIT_LOCATION' and contextcode='SETTINGS';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_EDIT_LOCATION', 'SETTINGS');

delete from "anv".permission_context where permissioncode = 'HRMS_REMOVE_LOCATION' and contextcode='SETTINGS';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_REMOVE_LOCATION', 'SETTINGS');