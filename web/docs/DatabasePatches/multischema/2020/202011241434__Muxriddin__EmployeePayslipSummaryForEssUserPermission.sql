delete from permission where code = 'HRMS_PAYSLIP_SUMMARY';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_PAYSLIP_SUMMARY', 'HRMS', 'View', 2, (select id from permission where code = 'HRMS_PAYSLIP_LIST'), 'HRMS_MODULE');

delete  from "anv".permission_context where permissioncode = 'HRMS_PAYSLIP_SUMMARY';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_PAYSLIP_SUMMARY', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_PAYSLIP_SUMMARY';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('HRMS_PAYSLIP_SUMMARY', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('HRMS_PAYSLIP_SUMMARY', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('HRMS_PAYSLIP_SUMMARY', 'ALLOW', 'HR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('HRMS_PAYSLIP_SUMMARY', 'ALLOW', 'ESS_USER');
