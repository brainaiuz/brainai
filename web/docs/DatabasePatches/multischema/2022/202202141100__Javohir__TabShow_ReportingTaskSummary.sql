


INSERT INTO "anv".reportingpermission(code, context, modulecode, name, parent, sorder)
values('REPORTING_SUMMARY_TAB_SHOW', 'REPORTING', 'REPORTING_SYSTEM', 'Customize Report', (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 101);


insert into "anv".permission_context (permissioncode, contextcode)
values ('REPORTING_SUMMARY_TAB_SHOW', 'REPORTING');

insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_SUMMARY_TAB_SHOW', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_SUMMARY_TAB_SHOW', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_SUMMARY_TAB_SHOW', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_SUMMARY_TAB_SHOW', 'ACCOUNTANT', 'ALLOW');