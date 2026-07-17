delete from permission where code='HRMS_SEE_ALL_INCIDENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values('HRMS_SEE_ALL_INCIDENT', 'HRMS', 'See All', 6, 403, 'EMPLOYEE_INCIDENTS');

delete from "anv".permission_context where permissioncode = 'HRMS_SEE_ALL_INCIDENT';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_SEE_ALL_INCIDENT', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_SEE_ALL_INCIDENT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_SEE_ALL_INCIDENT', 'ALLOW', 'ADMIN'),
                                                                           ('HRMS_SEE_ALL_INCIDENT', 'ALLOW', 'DR'),
                                                                           ('HRMS_SEE_ALL_INCIDENT', 'ALLOW', 'HR');




delete from permission where code='HRMS_SEE_OWN_INCIDENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values('HRMS_SEE_OWN_INCIDENT', 'HRMS', 'See Own', 7, 403, 'EMPLOYEE_INCIDENTS');

delete from "anv".permission_context where permissioncode = 'HRMS_SEE_OWN_INCIDENT';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_SEE_OWN_INCIDENT', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_SEE_OWN_INCIDENT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_SEE_OWN_INCIDENT', 'ALLOW', 'ADMIN'),
                                                                           ('HRMS_SEE_OWN_INCIDENT', 'ALLOW', 'DR'),
                                                                           ('HRMS_SEE_OWN_INCIDENT', 'ALLOW', 'HR');