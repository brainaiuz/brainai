insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_MC_CONVERT_TO_TASK', 'CRM', 'Convert to Task', (select count(id)
                                                             from permission
                                                             where parent = (select id from permission where code = 'CRM_CASES_LIST')) +
                                                            1,
        (select id from permission where code = 'CRM_CASES_LIST'),
        'CASE_MANAGEMENT');



insert into "anv".permission_context (permissioncode, contextcode)
values ('CRM_MC_CONVERT_TO_TASK', 'CRM');

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('CRM_MC_CONVERT_TO_TASK', 'ALLOW', 'PM'),
       ('CRM_MC_CONVERT_TO_TASK', 'ALLOW', 'ADMIN'),
       ('CRM_MC_CONVERT_TO_TASK', 'ALLOW', 'DR'),
       ('CRM_MC_CONVERT_TO_TASK', 'ALLOW', 'SALESMAN'),
       ('CRM_MC_CONVERT_TO_TASK', 'ALLOW', 'SALESPERSON'),
       ('CRM_MC_CONVERT_TO_TASK', 'ALLOW', 'CREATOR'),
       ('CRM_MC_CONVERT_TO_TASK', 'ALLOW', 'CLIENT'),
       ('CRM_MC_CONVERT_TO_TASK', 'ALLOW', 'CUSTOMER_SERVICE_REPRESENTATIVE');
