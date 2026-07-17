insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_ATTENDANCE_MARKS', 'HRMS', 'Attendance Marks', (select sorder from permission where code = 'HRMS_ATTENDANCE_TERMINAL_LIST' limit 1)+1, (select id from permission where code = 'HRMS_MAIN_MENU' limit 1), 'HRMS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_ATTENDANCE_MARKS', 'HRMS');

insert into "0".permission_context (permissioncode, contextcode)
values ('HRMS_ATTENDANCE_MARKS', 'HRMS');

