insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_TERMINAL_REPORT', 'HRMS', 'Terminal Report', (select sorder from permission where code ='HRMS_ATTENDANCE_REPORT' limit 1)+1, (select id from permission where code = 'HRMS_MAIN_MENU' limit 1), 'HRMS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_TERMINAL_REPORT', 'HRMS');

insert into "0".permission_context (permissioncode, contextcode)
values ('HRMS_TERMINAL_REPORT', 'HRMS');


insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_TERMINAL_ADJUST', 'HRMS', 'Adjust', 1, (select id from permission where code = 'HRMS_TERMINAL_ADJUST' limit 1), 'HRMS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_TERMINAL_ADJUST', 'HRMS');

insert into "0".permission_context (permissioncode, contextcode)
values ('HRMS_TERMINAL_ADJUST', 'HRMS');