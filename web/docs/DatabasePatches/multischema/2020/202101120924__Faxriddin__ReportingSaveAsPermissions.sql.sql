delete
from "anv".reportingpermission
where code in ('REPORTING_SAVE_AS_BUTTON');
insert into "anv".reportingpermission (code, context, name, sorder, parent, modulecode)
values ('REPORTING_SAVE_AS_BUTTON', 'REPORTING', 'Reporting Save As Button', 1, (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 'REPORTING_SYSTEM');


delete
from "anv".permission_context
where permissioncode in ('REPORTING_SAVE_AS_BUTTON');
insert into "anv".permission_context (permissioncode, contextcode)
values ('REPORTING_SAVE_AS_BUTTON', 'REPORTING');

delete
from "anv".rolepermission
where permissioncode in ('REPORTING_SAVE_AS_BUTTON');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_SAVE_AS_BUTTON', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_SAVE_AS_BUTTON', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_SAVE_AS_BUTTON', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_SAVE_AS_BUTTON', 'ACCOUNTANT', 'ALLOW');