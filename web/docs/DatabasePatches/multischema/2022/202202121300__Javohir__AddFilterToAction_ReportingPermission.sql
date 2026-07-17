


INSERT INTO "anv".reportingpermission(code, context, modulecode, name, parent, sorder)
values('REPORTING_FILTER_SHOW', 'REPORTING', 'REPORTING_SYSTEM', 'Filter', (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 100);


insert into "anv".permission_context (permissioncode, contextcode)
values ('REPORTING_FILTER_SHOW', 'REPORTING');

insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_FILTER_SHOW', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_FILTER_SHOW', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_FILTER_SHOW', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_FILTER_SHOW', 'ACCOUNTANT', 'ALLOW');