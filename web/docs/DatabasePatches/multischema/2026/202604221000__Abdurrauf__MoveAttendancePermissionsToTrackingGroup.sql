update permission
set parent = (select id from permission where code = 'HRMS_ATTENDANCE_TRACKING_TAB')
where code in ('HRMS_ATTENDANCE_MARKS', 'HRMS_ATTENDANCE_TERMINAL_LIST');

update permission set name = 'Marks' where code = 'HRMS_ATTENDANCE_MARKS';
update permission set name = 'Terminal List' where code = 'HRMS_ATTENDANCE_TERMINAL_LIST';
