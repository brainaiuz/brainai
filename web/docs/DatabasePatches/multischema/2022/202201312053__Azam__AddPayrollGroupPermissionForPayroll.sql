
--payroll group see all
delete from permission where code='PAYROLL_GROUP_SEE_ALL';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_GROUP_SEE_ALL', 'SETTINGS', 'See All', 2, (select id from permission where code = 'PAYROLL_GROUP_LIST'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_SEE_ALL';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_GROUP_SEE_ALL', 'SETTINGS');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_GROUP_SEE_ALL';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_GROUP_SEE_ALL', 'ALLOW', 'DR'),
                                                                           ('PAYROLL_GROUP_SEE_ALL', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_GROUP_SEE_ALL', 'ALLOW', 'HR');
--payroll group see own
delete from permission where code='PAYROLL_GROUP_SEE_OWN';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_GROUP_SEE_OWN', 'SETTINGS', 'See Own', 3, (select id from permission where code = 'PAYROLL_GROUP_LIST'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_SEE_OWN';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_GROUP_SEE_OWN', 'SETTINGS');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_GROUP_SEE_OWN';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_GROUP_SEE_OWN', 'ALLOW', 'DR'),
                                                                           ('PAYROLL_GROUP_SEE_OWN', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_GROUP_SEE_OWN', 'ALLOW', 'HR');

--payroll group edit
delete from permission where code='PAYROLL_GROUP_EDIT';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_GROUP_EDIT', 'SETTINGS', 'Edit', 5, (select id from permission where code = 'PAYROLL_GROUP_LIST'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_GROUP_EDIT';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_GROUP_EDIT', 'SETTINGS');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_GROUP_EDIT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_GROUP_EDIT', 'ALLOW', 'DR'),
                                                                           ('PAYROLL_GROUP_EDIT', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_GROUP_EDIT', 'ALLOW', 'HR');

--update sorder
update permission set sorder=4 where code='PAYROLL_GROUP_ADD';
update permission set sorder=6 where code='PAYROLL_GROUP_DELETE';