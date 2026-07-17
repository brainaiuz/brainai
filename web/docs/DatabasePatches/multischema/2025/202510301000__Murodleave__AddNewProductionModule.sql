


insert into "anv".mymodule (code, name, section, active)
values ('PRODUCTION', 'Production', 'accounting', true)
on conflict do nothing;

insert into "anv".container (code, defaultName, moduleCode, sorder, changed, preparedView)
values ('production', 'Production', 'accounting', 4, false, 'assemblyItems');

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('production', 'production', 'Production', 'Production', 'PRODUCTION', 'accounting', true) on conflict do nothing;;

insert into "anv".container_item (moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code = 'PRODUCTION' limit 1),
        (select id from "anv".property where objectName = 'production' limit 1),
        (select id from "anv".container where code = 'production' limit 1), 0, 'accounting');


delete from permission WHERE code = 'ACCOUNTING_PRODUCTION_MENU';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PRODUCTION_MENU';
delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PRODUCTION_MENU';

insert into permission (code, context, name, sorder, parent, modulecode)
select 'ACCOUNTING_PRODUCTION_MENU', 'ACCOUNTING', 'Production Menu', 3, p.id, 'ACCOUNTING_MODULE'
from permission p
where p.code = 'ACCOUNTING_MAIN_MENU';

insert into "anv".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PRODUCTION_MENU', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) VALUES ('ACCOUNTING_PRODUCTION_MENU', 'SETTINGS');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_PRODUCTION_MENU', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_PRODUCTION_MENU', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_PRODUCTION_MENU', 'ACCOUNTANT','ALLOW');

