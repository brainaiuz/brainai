-- AWS ga shart emas -- AWS ga shart emas -- AWS ga shart emas -- AWS ga shart emas -- AWS ga shart emas
delete from permission where code = 'SKIP_DEPARTMENT_ITEM_VALIDATION';
insert into permission (code, context, name, sorder, parent, modulecode)
    values ('SKIP_DEPARTMENT_ITEM_VALIDATION', 'ACCOUNTING', 'Skip department validation', 30, (select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), 'ACCOUNTING_MODULE');

delete from "anv".rolepermission where permissioncode = 'SKIP_DEPARTMENT_ITEM_VALIDATION';
insert into "anv".rolepermission (permissioncode, rolecode, access)
              values ('SKIP_DEPARTMENT_ITEM_VALIDATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
              values ('SKIP_DEPARTMENT_ITEM_VALIDATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access)
              values ('SKIP_DEPARTMENT_ITEM_VALIDATION', 'ACCOUNTANT', 'ALLOW');

delete from "0".rolepermission where permissioncode = 'SKIP_DEPARTMENT_ITEM_VALIDATION';
insert into "0".rolepermission (permissioncode, rolecode, access)
              values ('SKIP_DEPARTMENT_ITEM_VALIDATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access)
              values ('SKIP_DEPARTMENT_ITEM_VALIDATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access)
              values ('SKIP_DEPARTMENT_ITEM_VALIDATION', 'ACCOUNTANT', 'ALLOW');