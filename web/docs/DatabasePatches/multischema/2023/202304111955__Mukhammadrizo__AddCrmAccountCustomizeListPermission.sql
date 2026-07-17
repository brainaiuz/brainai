delete from permission where code='CRM_ACCOUNTS_CUSTOMIZE_LIST';
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_ACCOUNTS_CUSTOMIZE_LIST', 'CRM', 'f', 'Customize List', (select max(sorder)+1 from permission where parent = (select id from permission where code='CRM_ACCOUNTS_LIST')), (select id from permission where code='CRM_ACCOUNTS_LIST'), 'TASK_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'CRM_ACCOUNTS_CUSTOMIZE_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_CUSTOMIZE_LIST', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_ACCOUNTS_CUSTOMIZE_LIST';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_ACCOUNTS_CUSTOMIZE_LIST', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_ACCOUNTS_CUSTOMIZE_LIST', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_ACCOUNTS_CUSTOMIZE_LIST', 'ALLOW', 'SALESPERSON');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_ACCOUNTS_CUSTOMIZE_LIST', 'ALLOW', 'SALESMAN');




