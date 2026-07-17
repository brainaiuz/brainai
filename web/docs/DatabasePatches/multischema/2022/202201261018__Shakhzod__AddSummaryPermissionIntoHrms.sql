delete
from permission
where code = 'HRMS_DEPARTMENT_SUMMARY_VIEW';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_DEPARTMENT_SUMMARY_VIEW', 'SETTINGS', 'Summary', (select count(id)
                                                                from permission
                                                                where parent = (select id from permission where code = 'HRMS_EDIT_DEPARTMENT')) +
                                                               1,
        (select id from permission where code = 'HRMS_EDIT_DEPARTMENT'), 'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_DEPARTMENT_SUMMARY_VIEW', 'HRMS');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_DEPARTMENT_SUMMARY_VIEW', 'ALLOW', 'HR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_DEPARTMENT_SUMMARY_VIEW', 'ALLOW', 'DR');