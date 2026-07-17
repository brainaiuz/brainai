insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_GOAL_ADD_FROM_PROJECT', 'HRMS', 'Add Personal Goal from Project Goal Summary', (select count(id)
                                                                                              from permission
                                                                                              where parent = (select id from permission where code = 'HRMS_PERSONAL_GOALS')) +
                                                                                             1,
        (select id from permission where code = 'HRMS_PERSONAL_GOALS'), 'GOAL_MANAGEMENT');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_GOAL_ADD_FROM_PROJECT', 'HRMS');

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_GOAL_ADD_FROM_PROJECT', 'ALLOW', 'ADMIN');
