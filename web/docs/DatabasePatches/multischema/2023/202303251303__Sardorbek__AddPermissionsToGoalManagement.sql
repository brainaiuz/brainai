insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_GOAL_SEE_ALL', 'HRMS', 'See All Goals', (select count(id)
                                                       from permission
                                                       where parent = (select id from permission where code = 'HRMS_PERSONAL_GOALS')) +
                                                      1, (select id from permission where code = 'HRMS_PERSONAL_GOALS'),
        'GOAL_MANAGEMENT'),
       ('HRMS_GOAL_SEE_ALL_BY_DEPARTMENT', 'HRMS', 'See All Goals by department', (select count(id)
                                                                                   from permission
                                                                                   where parent = (select id from permission where code = 'HRMS_PERSONAL_GOALS')) +
                                                                                  1,
        (select id from permission where code = 'HRMS_PERSONAL_GOALS'), 'GOAL_MANAGEMENT'),
       ('HRMS_GOAL_SEE_ALL_BY_SUPERVISOR', 'HRMS', 'See All Goals by supervisor', (select count(id)
                                                                                   from permission
                                                                                   where parent = (select id from permission where code = 'HRMS_PERSONAL_GOALS')) +
                                                                                  1,
        (select id from permission where code = 'HRMS_PERSONAL_GOALS'), 'GOAL_MANAGEMENT');



insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_GOAL_SEE_ALL', 'HRMS'),
       ('HRMS_GOAL_SEE_ALL_BY_DEPARTMENT', 'HRMS'),
       ('HRMS_GOAL_SEE_ALL_BY_SUPERVISOR', 'HRMS');

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_GOAL_SEE_ALL', 'ALLOW', 'ADMIN'),
       ('HRMS_GOAL_SEE_ALL_BY_DEPARTMENT', 'ALLOW', 'ADMIN'),
       ('HRMS_GOAL_SEE_ALL_BY_SUPERVISOR', 'ALLOW', 'ADMIN');
