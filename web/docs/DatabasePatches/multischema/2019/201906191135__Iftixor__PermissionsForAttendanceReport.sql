
update permission set  sorder=3, parent=(select id from permission where code='HRMS_ATTENDANCE_REPORT'), name='Import Data'  where code='HRMS_IMPORT_ATTENDANCE_DATA';

delete from permission where code='HRMS_EXPORT_ATTENDANCE_DATA' and context='HRMS';
insert into permission(code, context, name, sorder, parent, modulecode) values ('HRMS_EXPORT_ATTENDANCE_DATA','HRMS','Export Data', 4, (select id from permission where code='HRMS_ATTENDANCE_REPORT'),'ATTENDING_TRACKING');

insert into "0".rolepermission(permissioncode,access,rolecode) values ('HRMS_EXPORT_ATTENDANCE_DATA','ALLOW','ADMIN');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('HRMS_EXPORT_ATTENDANCE_DATA','ALLOW','DR');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('HRMS_EXPORT_ATTENDANCE_DATA','ALLOW','HR');

delete from "0".permission_context where permissioncode = 'HRMS_EXPORT_ATTENDANCE_DATA' and contextcode='HRMS';
insert into "0".permission_context(permissioncode,contextcode) values ('HRMS_EXPORT_ATTENDANCE_DATA','HRMS');

delete from permission where code='HRMS_ADD_ATTENDANCE_TABLE_DATA' and context='HRMS';
insert into permission(code, context, name, sorder, parent, modulecode) values ('HRMS_ADD_ATTENDANCE_TABLE_DATA','HRMS','Add', 1, (select id from permission where code='HRMS_ATTENDANCE_REPORT'),'ATTENDING_TRACKING');

insert into "0".rolepermission(permissioncode,access,rolecode) values ('HRMS_ADD_ATTENDANCE_TABLE_DATA','ALLOW','ADMIN');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('HRMS_ADD_ATTENDANCE_TABLE_DATA','ALLOW','DR');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('HRMS_ADD_ATTENDANCE_TABLE_DATA','ALLOW','HR');

delete from "0".permission_context where permissioncode = 'HRMS_ADD_ATTENDANCE_TABLE_DATA' and contextcode='HRMS';
insert into "0".permission_context(permissioncode,contextcode) values ('HRMS_ADD_ATTENDANCE_TABLE_DATA','HRMS');

delete from permission where code='HRMS_EDIT_ATTENDANCE_TABLE_DATA' and context='HRMS';
insert into permission(code, context, name, sorder, parent, modulecode) values ('HRMS_EDIT_ATTENDANCE_TABLE_DATA','HRMS','Edit', 2, (select id from permission where code='HRMS_ATTENDANCE_REPORT'),'ATTENDING_TRACKING');

insert into "0".rolepermission(permissioncode,access,rolecode) values ('HRMS_EDIT_ATTENDANCE_TABLE_DATA','ALLOW','ADMIN');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('HRMS_EDIT_ATTENDANCE_TABLE_DATA','ALLOW','DR');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('HRMS_EDIT_ATTENDANCE_TABLE_DATA','ALLOW','HR');

delete from "0".permission_context where permissioncode = 'HRMS_EDIT_ATTENDANCE_TABLE_DATA' and contextcode='HRMS';
insert into "0".permission_context(permissioncode,contextcode) values ('HRMS_EDIT_ATTENDANCE_TABLE_DATA','HRMS');


insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_EXPORT_ATTENDANCE_DATA','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_EXPORT_ATTENDANCE_DATA','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_EXPORT_ATTENDANCE_DATA','ALLOW','HR');

delete from "anv".permission_context where permissioncode = 'HRMS_EXPORT_ATTENDANCE_DATA' and contextcode='HRMS';
insert into "anv".permission_context(permissioncode,contextcode) values ('HRMS_EXPORT_ATTENDANCE_DATA','HRMS');

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_ADD_ATTENDANCE_TABLE_DATA','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_ADD_ATTENDANCE_TABLE_DATA','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_ADD_ATTENDANCE_TABLE_DATA','ALLOW','HR');

delete from "anv".permission_context where permissioncode = 'HRMS_ADD_ATTENDANCE_TABLE_DATA' and contextcode='HRMS';
insert into "anv".permission_context(permissioncode,contextcode) values ('HRMS_ADD_ATTENDANCE_TABLE_DATA','HRMS');

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_EDIT_ATTENDANCE_TABLE_DATA','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_EDIT_ATTENDANCE_TABLE_DATA','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_EDIT_ATTENDANCE_TABLE_DATA','ALLOW','HR');

delete from "anv".permission_context where permissioncode = 'HRMS_EDIT_ATTENDANCE_TABLE_DATA' and contextcode='HRMS';
insert into "anv".permission_context(permissioncode,contextcode) values ('HRMS_EDIT_ATTENDANCE_TABLE_DATA','HRMS');