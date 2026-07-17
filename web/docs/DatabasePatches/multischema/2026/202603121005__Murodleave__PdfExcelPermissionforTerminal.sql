

delete from permission
where code = 'HRMS_EXPORT_PDF_ATTENDANCE_DATA' and context = 'HRMS';
insert into permission(code, context, name, sorder, parent, modulecode)
values ('HRMS_EXPORT_PDF_ATTENDANCE_DATA', 'HRMS', 'Export Pdf Data', 5,
        (select id from permission where code = 'HRMS_ATTENDANCE_REPORT' limit 1), 'ATTENDING_TRACKING');

insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_EXPORT_PDF_ATTENDANCE_DATA', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_EXPORT_PDF_ATTENDANCE_DATA', 'ALLOW', 'DR');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_EXPORT_PDF_ATTENDANCE_DATA', 'ALLOW', 'HR');

delete from "anv".permission_context
where permissioncode = 'HRMS_EXPORT_PDF_ATTENDANCE_DATA' and contextcode = 'HRMS';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_EXPORT_PDF_ATTENDANCE_DATA', 'HRMS');



--

delete from permission
where code = 'HRMS_EXPORT_PDF_TERMINAL_REPORT' and context = 'HRMS';
insert into permission(code, context, name, sorder, parent, modulecode)
values ('HRMS_EXPORT_PDF_TERMINAL_REPORT', 'HRMS', 'Export Pdf Data', (select sorder from permission where code ='HRMS_TERMINAL_REPORT' limit 1) + 1,
        (select id from permission where code = 'HRMS_MAIN_MENU' limit 1), 'HRMS_MODULE');

insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_EXPORT_PDF_TERMINAL_REPORT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_EXPORT_PDF_TERMINAL_REPORT', 'ALLOW', 'DR');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_EXPORT_PDF_TERMINAL_REPORT', 'ALLOW', 'HR');

delete from "anv".permission_context
where permissioncode = 'HRMS_EXPORT_PDF_TERMINAL_REPORT' and contextcode = 'HRMS';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_EXPORT_PDF_TERMINAL_REPORT', 'HRMS');

--

delete from permission
where code = 'HRMS_EXPORT_EXCEL_TERMINAL_REPORT' and context = 'HRMS';
insert into permission(code, context, name, sorder, parent, modulecode)
values ('HRMS_EXPORT_EXCEL_TERMINAL_REPORT', 'HRMS', 'Export Excel Data', (select sorder from permission where code ='HRMS_EXPORT_PDF_TERMINAL_REPORT' limit 1) + 1,
        (select id from permission where code = 'HRMS_MAIN_MENU' limit 1), 'HRMS_MODULE');

insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_EXPORT_EXCEL_TERMINAL_REPORT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_EXPORT_EXCEL_TERMINAL_REPORT', 'ALLOW', 'DR');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_EXPORT_EXCEL_TERMINAL_REPORT', 'ALLOW', 'HR');

delete from "anv".permission_context
where permissioncode = 'HRMS_EXPORT_EXCEL_TERMINAL_REPORT' and contextcode = 'HRMS';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_EXPORT_EXCEL_TERMINAL_REPORT', 'HRMS');
