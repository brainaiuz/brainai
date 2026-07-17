insert into permission (code, context, name, sorder, parent, modulecode)
values ('PM_PROJECT_WORKSTREAM_EDIT', 'PM', 'Project Edit Workstream', '23', (select id from permission where code='PM_PROJECT_LIST'), 'TASK_MANAGEMENT');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('PM_PROJECT_WORKSTREAM_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('PM_PROJECT_WORKSTREAM_EDIT', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('PM_PROJECT_WORKSTREAM_EDIT', 'DR', 'ALLOW');

insert into "anv".permission_context(permissioncode, contextcode) values('PM_PROJECT_WORKSTREAM_EDIT','PM');


insert into "0".rolepermission (permissioncode, rolecode, access) values ('PM_PROJECT_WORKSTREAM_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('PM_PROJECT_WORKSTREAM_EDIT', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('PM_PROJECT_WORKSTREAM_EDIT', 'DR', 'ALLOW');

insert into "0".permission_context(permissioncode, contextcode) values('PM_PROJECT_WORKSTREAM_EDIT','PM');
