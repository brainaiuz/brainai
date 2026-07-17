
--employee profile photo for hrms module
delete from permission where code='HRMS_EMPLOYEE_UPLOAD_PHOTO';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_EMPLOYEE_UPLOAD_PHOTO', 'HRMS', 'Upload Photo', (SELECT max(sorder) + 1 from permission WHERE code = 'HRMS_EMPLOYEES'),
        (select id from permission where code = 'HRMS_EMPLOYEES'), 'HRMS_MODULE');

delete from "anv".permission_context where permissioncode = 'HRMS_EMPLOYEE_UPLOAD_PHOTO';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_EMPLOYEE_UPLOAD_PHOTO', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_EMPLOYEE_UPLOAD_PHOTO';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_EMPLOYEE_UPLOAD_PHOTO', 'ALLOW', 'DR'),
                                                                           ('HRMS_EMPLOYEE_UPLOAD_PHOTO', 'ALLOW', 'ADMIN'),
                                                                           ('HRMS_EMPLOYEE_UPLOAD_PHOTO', 'ALLOW', 'HR');

--employee profile photo for pm module
delete from permission where code='PM_EMPLOYEE_UPLOAD_PHOTO';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('PM_EMPLOYEE_UPLOAD_PHOTO', 'PM', 'Upload Photo', (SELECT max(sorder) + 1 from permission WHERE code = 'PM_EMPLOYEE_LIST'),
        (select id from permission where code = 'PM_EMPLOYEE_LIST'), 'PM_MODULE');

delete from "anv".permission_context where permissioncode = 'PM_EMPLOYEE_UPLOAD_PHOTO';
insert into "anv".permission_context (permissioncode, contextcode) values ('PM_EMPLOYEE_UPLOAD_PHOTO', 'PM');

delete from "anv".rolepermission where permissioncode = 'PM_EMPLOYEE_UPLOAD_PHOTO';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PM_EMPLOYEE_UPLOAD_PHOTO', 'ALLOW', 'DR'),
                                                                           ('PM_EMPLOYEE_UPLOAD_PHOTO', 'ALLOW', 'ADMIN'),
                                                                           ('PM_EMPLOYEE_UPLOAD_PHOTO', 'ALLOW', 'HR');
