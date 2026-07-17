delete from permission where code in ('HRMS_ADD_TIMESLOT', 'HRMS_ADD_HOLIDAY');
insert into permission (code, context, name, sorder, parent, modulecode)
    values ('HRMS_ADD_TIMESLOT', 'HRMS', 'Add timeslot', 9,  (select id from permission where code = 'HRMS_ATTENDANCE_TRACKING_TAB'), 'CORE');

insert into permission (code, context, name, sorder, parent, modulecode)
    values ('HRMS_ADD_HOLIDAY', 'HRMS', 'Add holiday', 10,  (select id from permission where code = 'HRMS_ATTENDANCE_TRACKING_TAB'), 'CORE');

delete from "0".rolepermission where permissioncode in ('HRMS_ADD_TIMESLOT','HRMS_ADD_HOLIDAY');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_TIMESLOT', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_TIMESLOT', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_TIMESLOT', 'ALLOW', 'HR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_HOLIDAY', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_HOLIDAY', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_HOLIDAY', 'ALLOW', 'HR');

delete from "0".rolepermission where permissioncode in ('WORKSPACE_COMPANY_NEWS_ADD','WORKSPACE_COMPANY_NEWS_EDIT');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_ADD', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_ADD', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_ADD', 'ALLOW', 'HR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_EDIT', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_EDIT', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_EDIT', 'ALLOW', 'HR');




delete from "anv".rolepermission where permissioncode in ('HRMS_ADD_TIMESLOT','HRMS_ADD_HOLIDAY');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_TIMESLOT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_TIMESLOT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_TIMESLOT', 'ALLOW', 'HR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_HOLIDAY', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_HOLIDAY', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_ADD_HOLIDAY', 'ALLOW', 'HR');

delete from "anv".rolepermission where permissioncode in ('WORKSPACE_COMPANY_NEWS_ADD','WORKSPACE_COMPANY_NEWS_EDIT');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_ADD', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_ADD', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_ADD', 'ALLOW', 'HR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_EDIT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_EDIT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('WORKSPACE_COMPANY_NEWS_EDIT', 'ALLOW', 'HR');
