update permission set name = 'Summary' where code = 'HRMS_LEAVE_REQUEST_SUMMARY';

update permission set sorder = 3 where code = 'HRMS_REMOVE_REQUEST' and context = 'HRMS' and parent = 399;
update permission set sorder = 4 where code = 'HRMS_LEAVE_REQUEST_SUMMARY' and context = 'HRMS' and parent = 399;
update permission set sorder = 7 where code = 'HRMS_LEAVE_REQUEST_SEND_NOTIFICATION' and context = 'HRMS' and parent = 399;




delete from permission where code='HRMS_LEAVE_REQUEST_PDF_BUTTON';
insert into permission (code, context, name, sorder, parent, modulecode)
values('HRMS_LEAVE_REQUEST_PDF_BUTTON', 'HRMS', 'Pdf', 5, 399, 'LEAVE_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'HRMS_LEAVE_REQUEST_PDF_BUTTON';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_LEAVE_REQUEST_PDF_BUTTON', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_LEAVE_REQUEST_PDF_BUTTON';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_LEAVE_REQUEST_PDF_BUTTON', 'ALLOW', 'ADMIN'),
                                                                           ('HRMS_LEAVE_REQUEST_PDF_BUTTON', 'ALLOW', 'DR'),
                                                                           ('HRMS_LEAVE_REQUEST_PDF_BUTTON', 'ALLOW', 'HR');




delete from permission where code='HRMS_LEAVE_REQUEST_SUBMIT_BUTTON';
insert into permission (code, context, name, sorder, parent, modulecode)
values('HRMS_LEAVE_REQUEST_SUBMIT_BUTTON', 'HRMS', 'Submit', 6, 399, 'LEAVE_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'HRMS_LEAVE_REQUEST_SUBMIT_BUTTON';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_LEAVE_REQUEST_SUBMIT_BUTTON', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_LEAVE_REQUEST_SUBMIT_BUTTON';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_LEAVE_REQUEST_SUBMIT_BUTTON', 'ALLOW', 'ADMIN'),
                                                                           ('HRMS_LEAVE_REQUEST_SUBMIT_BUTTON', 'ALLOW', 'DR'),
                                                                           ('HRMS_LEAVE_REQUEST_SUBMIT_BUTTON', 'ALLOW', 'HR');