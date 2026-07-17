delete from permission where code = 'COPY_TASK';
insert into permission (code, context, name, sorder, parent, modulecode)
    values ('COPY_TASK', 'PM', 'Copy task to new', 20,  (select id from permission where code = 'PM_TASKS_LIST'), 'TASK_MANAGEMENT');

delete from "anv".rolepermission where permissioncode = 'COPY_TASK';
insert into "anv".rolepermission (permissioncode, access, rolecode)
                          values ('COPY_TASK', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
                          values ('COPY_TASK', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
                          values ('COPY_TASK', 'ALLOW', 'PM');

delete from "0".rolepermission where permissioncode = 'COPY_TASK';
insert into "0".rolepermission (permissioncode, access, rolecode)
                          values ('COPY_TASK', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode)
                          values ('COPY_TASK', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode)
                          values ('COPY_TASK', 'ALLOW', 'PM');
