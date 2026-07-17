-- INCIDENT_OWNER


delete from permission where code = 'INCIDENT_OWNER';

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('INCIDENT_OWNER', 'HRMS', false, 'Incident Owner', 40, (select id from permission where code = 'HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), false, 'HRMS_MODULE');

delete from "anv".rolepermission where permissioncode = 'INCIDENT_OWNER';

insert into "anv".rolepermission (permissioncode, rolecode,access) values('INCIDENT_OWNER','DR','ALLOW');

delete from "0".rolepermission where permissioncode = 'INCIDENT_OWNER';

insert into "0".rolepermission (permissioncode, rolecode,access) values('INCIDENT_OWNER','DR','ALLOW');
