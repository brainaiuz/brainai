delete from permission where code='HRMS_LEAVE_REQUEST_SUMMARY';
insert into permission (code, context, name, sorder, parent, modulecode)
values('HRMS_LEAVE_REQUEST_SUMMARY', 'HRMS', 'Leave Request Summary', 5, 399, 'LEAVE_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'HRMS_LEAVE_REQUEST_SUMMARY';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_LEAVE_REQUEST_SUMMARY', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_LEAVE_REQUEST_SUMMARY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_LEAVE_REQUEST_SUMMARY', 'ALLOW', 'ADMIN'),
                                                                           ('HRMS_LEAVE_REQUEST_SUMMARY', 'ALLOW', 'DR'),
                                                                           ('HRMS_LEAVE_REQUEST_SUMMARY', 'ALLOW', 'MEM');