update permission
set name = 'Import from MS'
where context = 'PM'
  and code = 'PM_PROJECT_LIST_IMPORT_BUTTON';
update permission
set code = 'PM_PROJECT_LIST_IMPORT_MS_BUTTON'
where context = 'PM'
  and code = 'PM_PROJECT_LIST_IMPORT_BUTTON';

insert into permission (code, context, name, sorder, parent, modulecode)
values ('PM_PROJECT_LIST_IMPORT_CSV_BUTTON', 'PM', 'Import from CSV',
        (select count(id) from permission where parent = (select id from permission where code = 'PM_PROJECT_LIST')) +
        1,
        (select id from permission where code = 'PM_PROJECT_LIST'), 'IMPORT_FROM_MS_PROJECT');

insert into "anv".permission_context (permissioncode, contextcode)
values ('PM_PROJECT_LIST_IMPORT_CSV_BUTTON', 'PM');

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('PM_PROJECT_LIST_IMPORT_CSV_BUTTON', 'ALLOW', 'ADMIN');
