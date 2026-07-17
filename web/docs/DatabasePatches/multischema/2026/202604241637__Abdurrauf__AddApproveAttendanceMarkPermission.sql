insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_APPROVE_ATTENDANCE_MARKS', 'HRMS', 'Approve Attendance Marks', 1, (select id from permission where code = 'HRMS_ATTENDANCE_MARKS' limit 1), 'HRMS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_APPROVE_ATTENDANCE_MARKS', 'HRMS');

insert into "0".permission_context (permissioncode, contextcode)
values ('HRMS_APPROVE_ATTENDANCE_MARKS', 'HRMS');
