delete
from "anv".reportingpermission
where code in ('REPORTING_ADD_EDIT_FOLDER');
insert into "anv".reportingpermission (code, context, name, sorder, parent, modulecode)
values ('REPORTING_ADD_EDIT_FOLDER', 'REPORTING', 'Reporting Add/Edit Folder', 1, (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 'REPORTING_SYSTEM');


delete
from "anv".permission_context
where permissioncode in ('REPORTING_ADD_EDIT_FOLDER');
insert into "anv".permission_context (permissioncode, contextcode)
values ('REPORTING_ADD_EDIT_FOLDER', 'REPORTING');

delete
from "anv".rolepermission
where permissioncode in ('REPORTING_ADD_EDIT_FOLDER');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_ADD_EDIT_FOLDER', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_ADD_EDIT_FOLDER', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_ADD_EDIT_FOLDER', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
values ('REPORTING_ADD_EDIT_FOLDER', 'ACCOUNTANT', 'ALLOW');