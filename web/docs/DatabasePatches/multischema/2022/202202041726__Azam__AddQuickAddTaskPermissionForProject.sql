delete from permission where code='PM_TASKS_QUICK_ADD';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PM_TASKS_QUICK_ADD', 'PM', 'Quick Add', 4, (select id from permission where code = 'PM_TASKS_LIST'),'TASK_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'PM_TASKS_QUICK_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('PM_TASKS_QUICK_ADD', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('PM_TASKS_QUICK_ADD', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode) values ('PM_TASKS_QUICK_ADD', 'PM');
insert into "anv".permission_context (permissioncode, contextcode) values ('PM_TASKS_QUICK_ADD', 'CRM');

delete from "anv".rolepermission where permissioncode = 'PM_TASKS_QUICK_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PM_TASKS_QUICK_ADD', 'ALLOW', 'MEM'),
                                                                             ('PM_TASKS_QUICK_ADD', 'ALLOW', 'ADMIN'),
                                                                             ('PM_TASKS_QUICK_ADD', 'ALLOW', 'PM');

update permission set sorder=3 where code='PM_TASKS_ADD';
update permission set sorder=5 where code='PM_TASKS_ADD_MULTI';
update permission set sorder=6 where code='PM_TASKS_EDIT';
update permission set sorder=7 where code='PM_TASKS_REMOVE';
update permission set sorder=8 where code='PM_TASKS_PDF_EXCEL_EXPORT';
update permission set sorder=9 where code='PM_TASK_LIST_CUSTOMIZE_BUTTON';
update permission set sorder=10 where code='PM_ASSIGN_TASK_TO_MEMBER';
update permission set sorder=11 where code='COPY_TASK';
update permission set sorder=12 where code='PM_TASK_LIST_IMPORT_BUTTON';
update permission set sorder=13 where code='SEE_ALL_TIME_ENTRIES';