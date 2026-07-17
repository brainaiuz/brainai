update permission set  sorder = 12 where code='HRMS_COMPANY_NEWS';

delete from permission where code='HRMS_NOTIFICATIONS';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_NOTIFICATIONS', 'HRMS', 'Notification', 11,
        (select id from permission where code = 'HRMS_SECTION_TAB'),'HRMS_MODULE');

delete from "anv".permission_context where permissioncode = 'HRMS_NOTIFICATIONS';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_NOTIFICATIONS', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_NOTIFICATIONS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_NOTIFICATIONS', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_NOTIFICATIONS', 'ALLOW', 'SALESMAN');
