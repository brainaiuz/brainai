delete from "anv".permission_context where permissioncode = 'HRMS_POSITION' and contextcode = 'SETTINGS';
insert into "anv".permission_context(permissioncode, contextcode) values('HRMS_POSITION','SETTINGS');
insert into "anv".permission_context(permissioncode, contextcode) values('HRMS_POSITION_EDIT','SETTINGS');
insert into "anv".permission_context(permissioncode, contextcode) values('HRMS_POSITION_REMOVE','SETTINGS');
insert into "anv".permission_context(permissioncode, contextcode) values('HRMS_POSITION_SUMMARRY','SETTINGS');
insert into "anv".permission_context(permissioncode, contextcode) values('HRMS_ADD_NEW_POSITION','SETTINGS');